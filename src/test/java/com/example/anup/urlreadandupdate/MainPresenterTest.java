package com.example.anup.urlreadandupdate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class MainPresenterTest {

    private MainPresenter presenter;

    @Mock
    private IModel repository;
    @Mock
    private IView viewContract;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        // Make presenter a mock while using mock repository and viewContract created above
        presenter = Mockito.spy(new MainPresenter(viewContract, repository));
    }

    @Test
    public void searchImages_noNetwork() {
        // Trigger
        when(viewContract.isNetworkAvailable())
                .thenReturn(false);
        presenter.loadContent();
        // Validation
        Mockito.verify(repository, Mockito.never()).getObservable();
    }


    @Test
    public void wordCountTest() {
        // validation
        long two = 2;
        assertEquals(presenter.findWordCount("abc bcd"), two);
    }
}

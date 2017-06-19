package com.deloitte.smt.controllertest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.List;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.deloitte.smt.SignalManagementApplication;

/**
 * Abstract Test class for any Controller based tests.
 *
 * @author RajeshKumar B
 * @since 17/6/2015
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SignalManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractControllerTest {

    protected MockMvc mockMvc;

    @Autowired
    private List<HandlerExceptionResolver> exceptionHandlerExceptionResolverList;

    @InjectMocks
    private MockHttpSession sessionMock;

    @InjectMocks
    private MockHttpServletRequest mockHttpServletRequest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = standaloneSetup(getController())
            .setHandlerExceptionResolvers(exceptionHandlerExceptionResolverList)
            .alwaysDo(print())
            .build();
    }

    /**
     * Get the Controller for this Controller Test.
     *
     * @return Controller class
     */
    public abstract Object getController();
}

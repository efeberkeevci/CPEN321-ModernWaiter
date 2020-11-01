package com.cpen321.modernwaiter;


import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import com.cpen321.modernwaiter.ui.menu.MenuFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(JUnit4.class)
@LargeTest
public class ExampleUnitTest {

    @Before
    public void init() {
    }

    @Test
    public void mockActivityExist() {
        FragmentScenario.launch(MenuFragment.class);
    }
}

package at.fhtw.tourplanner;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel {
    // https://en.wikipedia.org/wiki/Observer_pattern
    private List<FocusChangedListener> focusChangedListenerList = new ArrayList<FocusChangedListener>();

    public void addListener(FocusChangedListener listener) {
        this.focusChangedListenerList.add(listener);
    }


}

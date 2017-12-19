package ga.discoveryandlost.discoveryandlost.util;

import java.util.List;

import ga.discoveryandlost.discoveryandlost.Classifier;

/**
 * Created by tw on 2017. 8. 28..
 */

public interface OnDetecterListener {
    void onDetectFinish(List<Classifier.Recognition> results);
}

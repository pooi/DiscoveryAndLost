package ga.discoveryandlost.discoveryandlost.util;

import android.view.View;

import java.io.Serializable;

/**
 * Created by tw on 2017-05-05.
 */

public interface RegisterSelectListener extends Serializable {
    void select(int fragmentPosition, View v, int selectAnswerIndex, boolean force);
    void submit();
}

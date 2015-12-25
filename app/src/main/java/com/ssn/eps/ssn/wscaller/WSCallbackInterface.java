package com.ssn.eps.ssn.wscaller;

import java.util.Objects;

import model.Result;

/**
 * Created by lluis on 24/12/15.
 */
public interface WSCallbackInterface {
    void onProcesFinished(Result res);
}

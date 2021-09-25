package com.reeco.model;

import java.io.Serializable;

public interface Connection extends Serializable {
    Protocol getProtocol();
}
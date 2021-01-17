package pers.bo.zhao.action.proxy.service.impl;

import pers.bo.zhao.action.proxy.service.CountService;

public class CountServiceImpl implements CountService {

    private int count;

    @Override
    public int count() {
        return count++;
    }
}

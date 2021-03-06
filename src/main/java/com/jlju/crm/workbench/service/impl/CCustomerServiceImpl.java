package com.jlju.crm.workbench.service.impl;

import com.jlju.crm.workbench.dao.CustomerDao;
import com.jlju.crm.workbench.service.CustomerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CCustomerServiceImpl implements CustomerService {
    @Resource
    private CustomerDao customerDao;

    @Override
    public List<String> getCustomerName(String name) {

        List<String> sList=customerDao.getCustomerName(name);

        return sList;
    }
}

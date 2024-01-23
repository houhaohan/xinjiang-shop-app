package com.pinet.rest.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.core.constants.DB;
import com.pinet.rest.entity.CustomerAddress;
import com.pinet.rest.entity.dto.CustomerAddressDto;
import com.pinet.rest.entity.vo.AddressIdVo;
import com.pinet.rest.service.IAddressService;
import com.pinet.rest.service.ICustomerAddressService;
import com.pinet.rest.mapper.CustomerAddressMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author Administrator
* @description 针对表【customer_address(地址管理表)】的数据库操作Service实现
* @createDate 2022-12-06 16:58:34
*/
@Service
@DS(DB.SLAVE)
public class CustomerAddressServiceImpl extends ServiceImpl<CustomerAddressMapper, CustomerAddress> implements ICustomerAddressService {

    @Autowired
    private IAddressService addressService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(CustomerAddressDto customerAddressDto,Long userId) {
        if(customerAddressDto.getStatus() == 1){
            UpdateWrapper<CustomerAddress> wrapper = new UpdateWrapper<>();
            wrapper.eq("customer_id",userId);
            CustomerAddress item = new CustomerAddress();
            item.setStatus(0);
            update(item,wrapper);
        }

        AddressIdVo addressIdVo = addressService.selectIdByName(customerAddressDto.getProvince(), customerAddressDto.getCity(), customerAddressDto.getDistrict());
        CustomerAddress entity = new CustomerAddress();
        BeanUtils.copyProperties(customerAddressDto,entity);
        entity.setCreateTime(System.currentTimeMillis());
        entity.setProvinceId(addressIdVo.getProvinceId());
        entity.setCityId(addressIdVo.getCityId());
        entity.setDistrictId(addressIdVo.getDistrictId());
        StringBuffer sb = new StringBuffer();
        sb.append(entity.getProvince())
                .append(entity.getCity())
                .append(entity.getDistrict())
                .append(customerAddressDto.getAddressName());
        entity.setAddress(sb.toString());
        entity.setCustomerId(userId);
        entity.setUpdateTime(System.currentTimeMillis());

        //添加之前判断一下用户之前有没有地址  如果没有则设为默认
        List<CustomerAddress> customerAddresses = getByCustomerId(userId);
        if (customerAddresses == null || customerAddresses.size() == 0){
            entity.setStatus(1);
        }
        return this.save(entity);
    }

    @Override
    public boolean edit(CustomerAddressDto customerAddressDto,Long userId) {
        CustomerAddress entity = getById(customerAddressDto.getId());
        entity.setName(customerAddressDto.getName());
        entity.setPhone(customerAddressDto.getPhone());
        entity.setStatus(customerAddressDto.getStatus());
        entity.setProvince(customerAddressDto.getProvince());
        entity.setCity(customerAddressDto.getCity());
        entity.setDistrict(customerAddressDto.getDistrict());
        entity.setLat(customerAddressDto.getLat());
        entity.setLng(customerAddressDto.getLng());
        entity.setHouseNumber(customerAddressDto.getHouseNumber());
        entity.setAddress(customerAddressDto.getAddressName());
        AddressIdVo addressIdVo = addressService.selectIdByName(customerAddressDto.getProvince(), customerAddressDto.getCity(), customerAddressDto.getDistrict());
        entity.setProvinceId(addressIdVo.getProvinceId());
        entity.setCityId(addressIdVo.getCityId());
        entity.setDistrictId(addressIdVo.getDistrictId());
        StringBuffer sb = new StringBuffer();
        sb.append(entity.getProvince())
                .append(entity.getCity())
                .append(entity.getDistrict())
                .append(customerAddressDto.getAddressName());
        entity.setAddress(sb.toString());
        entity.setAddressName(customerAddressDto.getAddressName());
        entity.setCustomerId(userId);
        entity.setUpdateTime(System.currentTimeMillis());
        return this.updateById(entity);
    }

    @Override
    @Transactional
    public boolean updateDefaultAddress(Long id,Long userId) {

        CustomerAddress entity = new CustomerAddress();
        entity.setId(id);
        entity.setStatus(1);
        updateById(entity);

        UpdateWrapper<CustomerAddress> wrapper = new UpdateWrapper<>();
        wrapper.eq("customer_id",userId).ne("id",id);
        CustomerAddress item = new CustomerAddress();
        item.setStatus(0);
        update(item,wrapper);
        return true;
    }

    @Override
    public CustomerAddress getDefaultAddress(Long customerId) {
        QueryWrapper<CustomerAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_id",customerId).eq("status",1).orderByDesc("id").last("limit 1");
        return getOne(queryWrapper);
    }

    @Override
    public List<CustomerAddress> getByCustomerId(Long customerId) {
        LambdaQueryWrapper<CustomerAddress> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CustomerAddress::getCustomerId,customerId);
        return list(lambdaQueryWrapper);
    }


}





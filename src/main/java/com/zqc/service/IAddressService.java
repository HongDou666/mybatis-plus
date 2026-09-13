package com.zqc.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.zqc.domain.dto.AddressFormDTO;
import com.zqc.domain.po.Address;
import com.zqc.domain.vo.AddressVO;

import java.util.List;

/**
 * 收货地址业务接口。
 * 继承 {@link IService} 复用通用 CRUD；再声明面向接口的 DTO/VO 方法。
 */
public interface IAddressService extends IService<Address> {

    /** 新增地址 */
    void saveAddress(AddressFormDTO formDTO);

    /** 根据 id 删除地址（逻辑删除） */
    void deleteAddress(Long id);

    /** 根据 id 修改地址 */
    void updateAddress(AddressFormDTO formDTO);

    /** 根据 id 查询地址；不存在抛业务异常 */
    AddressVO queryAddressById(Long id);

    /** 根据用户 id 查询其收货地址列表 */
    List<AddressVO> queryAddressByUserId(Long userId);
}

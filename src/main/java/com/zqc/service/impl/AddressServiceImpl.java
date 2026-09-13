package com.zqc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.zqc.common.ResultCode;
import com.zqc.common.exception.BizException;
import com.zqc.domain.dto.AddressFormDTO;
import com.zqc.domain.po.Address;
import com.zqc.domain.vo.AddressVO;
import com.zqc.mapper.AddressMapper;
import com.zqc.service.IAddressService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收货地址业务实现。
 * 继承 {@link ServiceImpl}，复用 save / removeById / updateById / getById / lambdaQuery 等。
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

    /**
     * DTO → PO 后 save；主键由库表 AUTO_INCREMENT 生成
     */
    @Override
    public void saveAddress(AddressFormDTO formDTO) {
        if (formDTO == null || formDTO.getUserId() == null) {
            throw new BizException(ResultCode.BAD_REQUEST, "用户 id 不能为空");
        }
        Address address = BeanUtil.copyProperties(formDTO, Address.class);
        // 新增不带 id，避免误传
        address.setId(null);
        save(address);
    }

    /**
     * 逻辑删除（@TableLogic / 全局 logic-delete 配置）
     */
    @Override
    public void deleteAddress(Long id) {
        if (id == null) {
            throw new BizException(ResultCode.BAD_REQUEST, "地址 id 不能为空");
        }
        boolean removed = removeById(id);
        if (!removed) {
            throw new BizException(ResultCode.ADDRESS_NOT_FOUND, "收货地址不存在，id=" + id);
        }
    }

    /**
     * 按 id 更新；先校验存在再 updateById
     */
    @Override
    public void updateAddress(AddressFormDTO formDTO) {
        if (formDTO == null || formDTO.getId() == null) {
            throw new BizException(ResultCode.BAD_REQUEST, "地址 id 不能为空");
        }
        Address exists = getById(formDTO.getId());
        if (exists == null) {
            throw new BizException(ResultCode.ADDRESS_NOT_FOUND, "收货地址不存在，id=" + formDTO.getId());
        }
        Address address = BeanUtil.copyProperties(formDTO, Address.class);
        updateById(address);
    }

    /**
     * getById → AddressVO；不存在抛 ADDRESS_NOT_FOUND
     */
    @Override
    public AddressVO queryAddressById(Long id) {
        if (id == null) {
            throw new BizException(ResultCode.BAD_REQUEST, "地址 id 不能为空");
        }
        Address address = getById(id);
        if (address == null) {
            throw new BizException(ResultCode.ADDRESS_NOT_FOUND, "收货地址不存在，id=" + id);
        }
        return BeanUtil.copyProperties(address, AddressVO.class);
    }

    /**
     * 按 userId 查询地址列表（逻辑删除的不会查出）
     */
    @Override
    public List<AddressVO> queryAddressByUserId(Long userId) {
        if (userId == null) {
            throw new BizException(ResultCode.BAD_REQUEST, "用户 id 不能为空");
        }
        List<Address> list = lambdaQuery()
                .eq(Address::getUserId, userId)
                .list();
        return BeanUtil.copyToList(list, AddressVO.class);
    }
}

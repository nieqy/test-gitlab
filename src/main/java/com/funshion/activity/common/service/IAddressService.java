/**
 * IActivityAddressService.java
 * com.funshion.activity.common.service
 * <p>
 * Function： TODO
 * <p>
 * ver     date      		author
 * ──────────────────────────────────
 * 2019年1月18日 		xiaowei
 * <p>
 * Copyright (c) 2019, TNT All Rights Reserved.
 */

package com.funshion.activity.common.service;

import com.funshion.activity.common.bean.AddressInfo;
import com.funshion.activity.common.constants.Result;

/**
 * @ClassName:IActivityAddressService
 * @Function: TODO ADD FUNCTION
 * @Reason: TODO ADD REASON
 *
 * @author xiaowei
 * @version
 * @since Ver 1.1
 * @Date 2019年1月18日        下午2:04:23
 *
 * @see
 *
 */
public interface IAddressService {

    public String getActivityType();

    public Result<?> saveAddress(AddressInfo info);

    public Result<?> getAddressStatus(AddressInfo info);
}


/**
 * SignController.java
 * com.funshion.weixin.controller
 * <p>
 * Function： TODO
 * <p>
 * ver     date      		author
 * ──────────────────────────────────
 * 2018年7月9日 		nieqy
 * <p>
 * Copyright (c) 2018, TNT All Rights Reserved.
 */

package com.funshion.activity.wx;

import com.funshion.activity.common.constants.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author nieqy
 * @ClassName:SignController
 * @Function: TODO ADD FUNCTION
 * @Reason: TODO ADD REASON
 * @Date 2018年7月9日        下午3:47:55
 * @see
 * @since Ver 1.1
 */
@Controller
@RequestMapping("/api/sign")
public class SignController {

    @RequestMapping("/wx")
    @ResponseBody
    public Result<?> index(Model model, HttpServletRequest request) {
        String strBackUrl = request.getRequestURL().toString();
        if (request.getQueryString() != null) {
            strBackUrl += "?" + (request.getQueryString()); //参数
        }
        try {
            Map<String, String> params = Sign.sign(Sign.getTicketToken(), strBackUrl);
            return Result.getSuccessResult(params);
        } catch (Exception e) {
            return Result.SYSTEM_ERROR;
        }
    }

}


package com.pinet.rest.controller;


import com.pinet.core.page.PageRequest;
import com.pinet.core.result.Result;
import com.pinet.core.version.ApiVersion;
import com.pinet.rest.entity.ExchangeProduct;
import com.pinet.rest.entity.ExchangeRecord;
import com.pinet.rest.entity.dto.ExchangeProductListDto;
import com.pinet.rest.entity.vo.ExchangeRecordListVo;
import com.pinet.rest.service.IExchangeRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.pinet.core.controller.BaseController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 兑换记录 前端控制器
 * </p>
 *
 * @author wlbz
 * @since 2024-01-16
 */
@RestController
@RequestMapping("/{version}/exchange_record")
@Api(tags = "兑换记录")
public class ExchangeRecordController extends BaseController {
    @Resource
    private IExchangeRecordService exchangeRecordService;

    @PostMapping("/exchangeRecordList")
    @ApiVersion(1)
    @ApiOperation("兑换记录")
    public Result<List<ExchangeRecordListVo>> exchangeRecordList(@RequestBody PageRequest request){
        List<ExchangeRecordListVo> exchangeRecordList = exchangeRecordService.exchangeRecordList(request);
        return Result.ok(exchangeRecordList);
    }

}

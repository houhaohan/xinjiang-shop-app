package com.pinet.rest.controller;


import com.pinet.core.page.PageRequest;
import com.pinet.core.result.Result;
import com.pinet.core.version.ApiVersion;
import com.pinet.rest.entity.ScoreRecord;
import com.pinet.rest.service.IScoreRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.pinet.core.controller.BaseController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 积分明细 前端控制器
 * </p>
 *
 * @author wlbz
 * @since 2023-12-22
 */
@RestController
@RequestMapping("/{version}/score_record")
@Api(tags = "积分")
public class ScoreRecordController extends BaseController {
    @Resource
    private IScoreRecordService scoreRecordService;

    @PostMapping("/pageList")
    @ApiVersion(1)
    @ApiOperation("积分明细")
    public Result<List<ScoreRecord>> list(@RequestBody PageRequest pageRequest){
        List<ScoreRecord> scoreRecords = scoreRecordService.pageList(pageRequest);
        return Result.ok(scoreRecords);
    }



}

package site.alanliang.geekblog.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import site.alanliang.geekblog.anntation.OperationLog;
import site.alanliang.geekblog.common.Constant;
import site.alanliang.geekblog.common.JsonResult;
import site.alanliang.geekblog.common.TableResult;
import site.alanliang.geekblog.model.Visitor;
import site.alanliang.geekblog.query.UserQuery;
import site.alanliang.geekblog.service.VisitorService;
import site.alanliang.geekblog.utils.MD5Util;
import site.alanliang.geekblog.vo.VisitorLoginVO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Descriptin TODO
 * @Author AlanLiang
 * Date 2020/5/20 22:05
 * Version 1.0
 **/
@RestController
@RequestMapping("/admin/visitor")
public class VisitorController {

    @Autowired
    private VisitorService visitorService;

    /**
     * 将日期格式的String类型转为Date类型
     *
     * @param binder 数据绑定
     */
    @InitBinder
    public void dateBinder(WebDataBinder binder) {
        String pattern = "yyyy-MM-dd";
        CustomDateEditor editor = new CustomDateEditor(new SimpleDateFormat(pattern), true);
        binder.registerCustomEditor(Date.class, editor);
    }

    @PreAuthorize("hasAuthority('sys:visitor:query')")
    @OperationLog("查询访客")
    @GetMapping
    public TableResult listByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                  @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                                  UserQuery userQuery) {
        Page<Visitor> pageInfo = visitorService.listTableByPage(page, limit, userQuery);
        return TableResult.tableOk(pageInfo.getRecords(), pageInfo.getTotal());
    }


    @PreAuthorize("hasAuthority('sys:visitor:status')")
    @OperationLog("修改访客状态")
    @PutMapping("/status")
    public JsonResult changeStatus(@NotNull @RequestParam("id") Long id) {
        visitorService.changeStatus(id);
        return JsonResult.ok();
    }

    @PreAuthorize("hasAuthority('sys:visitor:delete')")
    @OperationLog("删除访客")
    @DeleteMapping("/{id}")
    public JsonResult remove(@NotNull @PathVariable("id") Long id) {
        visitorService.removeById(id);
        return JsonResult.ok();
    }

    @PreAuthorize("hasAuthority('sys:visitor:delete')")
    @OperationLog("批量删除访客")
    @DeleteMapping
    public JsonResult removeBatch(@NotEmpty @RequestBody List<Long> idList) {
        visitorService.removeByIdList(idList);
        return JsonResult.ok();
    }
}

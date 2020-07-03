/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//设置全局变量
var time = 2000;
//初始化iCheck
$(function () {
    iCheckInitFunction();

    var config = {
        '.chosen-select': {},
        '.chosen-select-deselect': {
            allow_single_deselect: true
        },
        '.chosen-select-no-single': {
            disable_search_threshold: 10
        },
        '.chosen-select-no-results': {
            no_results_text: 'Oops, nothing found!'
        },
        '.chosen-select-width': {
            width: "95%"
        }
    }
    for (var selector in config) {
        $(selector).chosen(config[selector]);
    }
});

//初始化iCheck 方法
function iCheckInitFunction() {
    $('.i-checks').iCheck({
        checkboxClass: 'icheckbox_square-green',
        radioClass: 'iradio_square-green'
    });
}

function TableiCheck(theadSelector, tbodySelector) {
    //每次表格重绘取消表格头选中
    $(theadSelector).iCheck('uncheck');
    $(theadSelector).on('ifChecked', function (event) { //ifCreated 事件应该在插件初始化之前绑定
        $(tbodySelector).iCheck('check');
    });
    $(theadSelector).on('ifUnchecked', function (event) { //ifCreated 事件应该在插件初始化之前绑定
        $(tbodySelector).iCheck('uncheck');
    });
}

//打开对话框(添加修改)
function openLayer(title, url, width, height) {
    if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {//如果是移动端，就使用自适应大小弹窗
        width = 'auto';
        height = 'auto';
    }
    top.layer.open({
        type: 2,
        area: [width, height],
        title: title,
        maxmin: true, //开启最大化最小化按钮
        content: url,
        scrollbar: false
    });
}


/**
 * 确认提示框 刷新页面
 * @param {type} mess
 * @param {type} href
 * @returns {undefined}
 */
function confirmLayer(mess, href) {
    top.layer.confirm(mess, {icon: 3, title: "系统提示"}, function (index) {
        if (href == "") {
            top.layer.alert("URL地址错误", {icon: 3});
        } else {
            var load_index = top.layer.load();
            $.ajax({
                type: "POST",
                url: href,
                dataType: "json",
                success: function (data) {
                    top.layer.close(load_index);
                    if (data.code === "success") {
                        top.layer.msg(data.message, {icon: 1, time: time});
                        timeoutLayer(time, true);
                    } else {
                        top.layer.alert(data.message, {icon: 3});
                    }
                }
            });
        }
    });
}

/**
 * 确认提示框 ReloadTable
 * @param {type} mess 提示信息
 * @param {string} href 链接地址
 * @param {type} dataTable DateTable 对象
 * @returns {undefined}
 */
function confirmLayerReloadTable(mess, href, dataTable) {
    top.layer.confirm(mess, {icon: 3, title: "系统提示"}, function (index) {
        if (href == "") {
            top.layer.alert("URL地址错误", {icon: 3});
        } else {
            var load_index = top.layer.load();
            $.ajax({
                type: "POST",
                url: href,
                dataType: "json",
                success: function (data) {
                    top.layer.close(load_index);
                    if (data.code === "success") {
                        top.layer.msg(data.message, {icon: 1, time: time});
                        setTimeout(function () {
                            top.layer.closeAll();
                            dataTable.draw(false);
                        }, time);
                    } else {
                        top.layer.alert(data.message, {icon: 3});
                    }
                }
            });
        }
    });
}

/**
 * 定时关闭Layer 
 * @param {type} time 关闭等待时间
 * @param {type} isRefresh 是否开启刷新当前窗口
 * @returns {undefined}
 */
function timeoutLayer(time, isRefresh) {
    setTimeout(function () {
        top.layer.closeAll();
        if (isRefresh == true) {
            refreshActiveIframe();
        }
    }, time);
}

/**
 * 通用批量删除方法
 * @param {type} url
 * @param {type} param 可以是对象 {"aa":11,"bb":22} 也可以是 aa=11&bb=22
 * @param {type} mess 确认提示框
 * @returns {undefined}
 */
function commonHandleAll(url, param, mess) {
    top.layer.confirm(mess, {icon: 3, title: "系统提示"}, function (index) {
        var load_index = top.layer.load();
        $.ajax({
            type: 'POST',
            url: url,
            data: param,
            dataType: 'json',
            success: function (data) {
                top.layer.close(load_index);
                if (data.code === 'success') {
                    top.layer.msg(data.message, {icon: 6, time: time});
                    timeoutLayer(time, true);
                } else {
                    top.layer.msg(data.message, {icon: 3, time: time});
                }
            },
        });
    });
}

function commonDeleteAllReloadTable(url, param, mess, dataTable, headers) {
    top.layer.confirm(mess, {icon: 3, title: "系统提示"}, function (index) {
        var load_index = top.layer.load();
        $.ajax({
            type: 'POST',
            url: url,
            data: param,
            dataType: 'json',
            headers: headers,
            success: function (data) {
                top.layer.close(load_index);
                if (data.code === 'success') {
                    top.layer.msg(data.message, {icon: 6, time: time});
                    setTimeout(function () {
                        top.layer.closeAll();
                        dataTable.draw(false);
                    }, time);
                } else {
                    top.layer.msg(data.message, {icon: 3, time: time});
                }
            },
        });
    });
}

/** 数据库字典值匹配方法 无点击事件
 *
 * datas 通过thymeleaf 标签读取的 ${@thymeleafDictService.selectDictData('xxx')} 数组对象
 * value 渲染的值
 * icon 显示的图标（可以为空）
 *
 * **/
function selectDictLabel(datas, value, icon) {
    var actions = "";
    var iconCode = "<i class='fa "+icon+"'></i>";
    if(!icon){
        iconCode = "";
    }
    $.each(datas, function(index, dict) {
        if (dict.dictValue == value) {
            actions = actions + "<small class='label label-"+ dict.cssClass +"'>" +iconCode + " " + dict.dictLabel + "</small>";
            return false;
        }
    });
    return actions;
}

/** 数据库字典值匹配方法 有点击事件
 *
 * datas 通过thymeleaf 标签读取的 ${@thymeleafDictService.selectDictData('xxx')} 数组对象
 * value 渲染的值
 * id 数据主键
 * icon 显示的图标（可以为空）
 * customClick 两种事件 一个是确认
 *
 * **/
function selectDictUpdate(datas, value, id, icon) {
    var actions = "";
    var iconCode = "<i class='fa "+icon+"'></i>";
    if(!icon){
        iconCode = "";
    }
    $.each(datas, function(index, dict) {
        if (dict.dictValue == value) {
            actions = actions + "<button type='button' class='btn btn-"+ dict.cssClass +" btn-xs' onclick=\"updateStatus('"+id+"','"+value+"')\">" +iconCode + " " + dict.dictLabel + "</button>";
            return false;
        }
    });
    return actions;
}

/**
 * 百度上传方法
 * @param id 外层ID
 * @param folder 上传文件夹
 * @param lable 按钮标签
 * @param width
 * @param height
 */
function setUploader(id, folder, lable, width, height) {
    var $input = $("#" + id + " input[name=\"" + id + "\"]");
    var $img = $("#" + id + " .picture");
    var pickid = '#' + id + " .pick";
    var pickLable = "点击选择图片";
    if(lable){
        pickLable = lable;
    }
    var uploader = WebUploader.create({
        auto: true,
        swf: 'lib/webuploader/0.1.5/Uploader.swf',
        // 文件接收服务端。
        server: ctx + 'common/upload',
        formData: { "folderName": folder},
        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: {
            id: pickid,
            label: pickLable,
            multiple: false//默认为true，true表示可以多选文件
        },
        // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
        resize: false,
        // 只允许选择图片文件。
        accept: {
            title: 'Images',
            extensions: 'gif,jpg,jpeg,bmp,png',
            mimeTypes: 'image/*'
        },
        duplicate :true  //允许重复提交
    });

    uploader.on('uploadSuccess', function (file, response) {
        if (response.code == "success") {
            layer.msg(response.message);
            if($input){
                $input.val(response.params.url);
            }
            if($img){
                var url = response.params.url;
                if(url){
                    $img.attr("src", ctx + url.substring(1, url.length));
                }
            }
        } else {
            layer.msg(response.message);
        }
        // 创建缩略图
        // 如果为非图片文件，可以不用调用此方法。
        // thumbnailWidth x thumbnailHeight 为 100 x 100
        // uploader.makeThumb(file, function (error, src) {
        //     if ( error ) {
        //         layer.msg("预览缩略图错误");
        //     } else {
        //         if($img){
        //             $img.attr("src", src);
        //         }
        //     }
        // }, width, height);
    });
};

function formatDate(date, fmt) {
    if (typeof date == 'string') {
        return date;
    }

    if (!fmt) fmt = "yyyy-MM-dd HH:mm:ss";

    if (!date || date == null) return null;
    var o = {
        'M+': date.getMonth() + 1, // 月份
        'd+': date.getDate(), // 日
        'H+': date.getHours(), // 小时
        'm+': date.getMinutes(), // 分
        's+': date.getSeconds(), // 秒
        'q+': Math.floor((date.getMonth() + 3) / 3), // 季度
        'S': date.getMilliseconds() // 毫秒
    }
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
    for (var k in o) {
        if (new RegExp('(' + k + ')').test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)))
    }
    return fmt
}

/**
 * 生成随机数
 * @param n
 * @returns {string}
 */
var chars = ['0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'];
function generateMixed(n) {
    var res = "";
    for(var i = 0; i < n ; i ++) {
        var id = Math.ceil(Math.random()*35);
        res += chars[id];
    }
    return res;
}


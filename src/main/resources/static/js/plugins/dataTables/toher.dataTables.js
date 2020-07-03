/**
 * @summary     ToherDataTables
 * @description 封装DataTables包含可编辑表格、选择、滚动加载
 * @version     1.1.1
 * @file        toher.dataTables.js
 * @author      李怀明
 * @copyright   Copyright 2018-2020 TOHER Ltd.
 *
 */
/**
 * 获取JS文件的路径
 * @type HTMLCollection|Document.scripts|Node.scripts|HTMLDocument.scripts
 */
var path = document.scripts;
path = path[path.length - 1].src.substring(0, path[path.length - 1].src.lastIndexOf("/") + 1);
var edit_disabled = "edit_disabled";
document.write("<script src='" + path + "jquery.dataTables.js'></script>");
document.write("<script src='" + path + "dataTables.bootstrap.js'></script>");
document.write("<script src='" + path + "dataTables.editor.min.js'></script>");
var load = '<div class="layui-layer layui-anim layui-layer-loading " id="layui-layer1" type="loading" times="1" showtime="0" contype="string" style="z-index: 19891015; top: 212.5px; left: 820px;"><div class="layui-layer-content layui-layer-loading3"></div><span class="layui-layer-setwin"></span></div>';
(function ($, window, document, undefined) {
    "use strict";
    var dataTable;
    var defaults = {
        edit: false,
        editFiles: [],
        editPostAjaxUrl: null,
        dom: "lrtip",
        iCheck: false,

        select: false,

        getDataUrl: null,
        scrollX: false,
        scrollY: null,
        columns: null,
        columnDefs: [],
        lengthMenu: null
    };
    function ToherDataTables($ele, options) {
        this.$ele = $ele;
        this.options = options = $.extend(defaults, options || {});
        this.init();
    }

    ToherDataTables.prototype = {
        constructor: ToherDataTables,
        /**
         * 初始化方法
         */
        init: function () {
            this.createTable();
        },
        createTable: function () {
            //获取配置参数
            var options = this.options;
            var toherTable = this;
            /**
             * 开启可编辑表格功能
             */
            console.log(this.$ele.selector);
            //定义初始化参数的赋值
            var cols = [];
            var files = options.editFiles;
            //如果开启了编辑，files.lenght==0 即没有传递编辑字段时候，默认取dataTable数据渲染列
            if (files.length === 0 && options.edit === true) {
                files = options.columns;
            }
            //判断是否已经设置了可编辑列，没有则取dataTable数据渲染列
            var eFiles = [];
            if (options.columns === undefined || options.columns.length == 0 || options.columns == null) {
                throw err = new Error('定义数据渲染列值错误');
                console.log("ToherDataTables : " + err);
            } else {
                //设定可编辑列
                if (files.length !== 0) {
                    for (var i = 0; i < files.length; i++) {
                        console.log("files[i]" + files[i]);
                        if (files[i] !== null) {
                            var data = {name: files[i]};
                            eFiles.push(data);
                        }
                    }
                    options.editFiles = eFiles;
                    console.log("==1" + eFiles);
                }
                //对dataTable 进行数据渲染
                for (var j = 0; j < options.columns.length; j++) {
                    var data;
                    //数据源绑定的数据为null则代表默认自动一显示字段，非可编辑字段
                    if (options.columns[j] !== null) {
                        //迭代绑定数据列值 和 用户设定可编辑列 存在则可以编辑
                        data = {"data": options.columns[j], "className": edit_disabled};
                        for (var i = 0; i < files.length; i++) {
                            if (options.columns[j] === files[i]) {
                                data = {"data": options.columns[j]};
                                break;
                            }
                        }
                    } else {
                        data = {"data": options.columns[j], "className": edit_disabled};
                    }
                    cols.push(data);
                }
            }

            dataTable = this.$ele.DataTable({
                //改变加载提示
                "language": {"processing": load},
                /**
                 l - Length changing 改变每页显示多少条数据的控件
                 f - Filtering input 即时搜索框控件
                 t - The Table 表格本身
                 i - Information 表格相关信息控件
                 p - Pagination 分页控件
                 r - pRocessing 加载等待显示信息
                 **/
                "dom": options.dom,
                "serverSide":true,
                "lengthMenu": [10, 20, 50, 100],
                "ordering": false, //禁用排序
                "processing": true,
                "ajax": {url: options.getDataUrl},
                "columns": cols,
                "columnDefs": options.columnDefs,
                //i-checks ,每次重绘需要再次初始化
                "drawCallback": function (settings) {
                    toherTable.openICheck();
                }
            });
            this.openEditTable();
        },
        openEditTable: function () {
            //获取配置参数
            var options = this.options;
            if (options.edit = true) {
                var editor = new $.fn.dataTable.Editor({
                    ajax: options.editPostAjaxUrl,
                    //获取$选择器的名称
                    table: this.$ele.selector,
                    fields: options.editFiles
                })
                $(this.$ele.selector).on('click', 'tbody tr td:not(".' + edit_disabled + '")', function (e) {
                    editor.inline(this);
                });
            }
        },
        openICheck: function () {
            var options = this.options;
            if (options.iCheck === true) {
                $('.i-checks').iCheck({
                    checkboxClass: 'icheckbox_square-green',
                    radioClass: 'iradio_square-green',
                });
                var selector = this.$ele.selector;
                $(selector + ' thead tr th input.i-checks').on('ifChecked', function (event) { //ifCreated 事件应该在插件初始化之前绑定 
                    $(selector + ' tbody tr td input.i-checks').iCheck('check');
                });

                $(selector + ' thead tr th input.i-checks').on('ifUnchecked', function (event) { //ifCreated 事件应该在插件初始化之前绑定
                    $(selector + ' tbody tr td input.i-checks').iCheck('uncheck');
                });
            }
        },
        getDataTable : function(){
            return dataTable;
        }
    }
    $.fn.ToherDataTables = function (options) {
        options = $.extend(defaults, options || {});
        return new ToherDataTables($(this), options);
    }
})(jQuery, window, document);
    
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title><fmt:message key="language.Restaurant_Management"/>-<fmt:message key="language.Feedbacks"/></title>
    <jsp:include page="/meta.jsp"/>
</head>
<body style="width:98%;height:95%;">
<div id="mainsearch" style="width: 100%">
    <div class="searchbox">
        搜索内容: <input id="a_contnet" class="mini-textbox" emptyText="请输入需要搜索的内容" style="width:150px;"/>&nbsp;&nbsp;&nbsp;&nbsp;
        <%-- <fmt:message key="language.Feedback_Time"/>: <input id="ur_time1" style="width:170px;" class="mini-datepicker" allowInput="false"
                     showTodayButton="false" format="yyyy-MM-dd H:mm:ss" timeFormat="H:mm:ss" emptyText="<fmt:message key='language.Feedback_Time'/>"
                     showTime="true"/>
        — <input id="ur_time2" style="width:170px;" class="mini-datepicker" allowInput="false" showTodayButton="false"
                 format="yyyy-MM-dd H:mm:ss" timeFormat="H:mm:ss" emptyText="<fmt:message key='language.Feedback_Time'/>" showTime="true"/>
        <span>&nbsp;&nbsp;&nbsp;</span> --%>
        <input type="button" value="<fmt:message key='language.Search'/>" onclick="search()"/>
        <span>&nbsp;</span>
        <input type="button" value="<fmt:message key='language.Reset'/>" onclick="reset()"/>

        <p>
            <!-- <div style="padding-bottom: 1px;">
            </div> -->
    </div>
</div>

<div class="mini-fit">
    <div id="userReplyGrid" class="mini-datagrid" style="width:100%;height:100%;" allowResize="true"
         url="${ctx }/rbac/app/listAll" idField="ur_id" multiSelect="true">
        <div property="columns">
            <div type="indexcolumn" align="center"><fmt:message key="language.Index"/></div>
            <div field="tf_id" width="80" headerAlign="center" align="center" allowSort="true">反馈编号</div>
            <div field="app_name" width="50" headerAlign="center" align="center" allowSort="true">应用名称</div>
            <div field="app_content" width="120" headerAlign="center" renderer="onView" align="center" allowSort="false">反馈内容</div>
        </div>
    </div>
</div>



<div id="detailWindow" class="mini-window" title="详情" style="width:340px;height:200px;" showModal="true" allowResize="true"
     allowDrag="true">
    <div id="detailform" class="form">
        <table style="width:100%;">
            <tr>
                <td style="width:80px;" align="right">应用名称:</td>
                <td style="width:150px;">
                    <input id="app_name" name="app_name" class="mini-textbox" style="width:150px;" />
                </td>
            </tr>
            <tr>
                <td style="width:80px;" align="right">反馈内容:</td>
                <td style="width:150px;">
                    <textarea id="app_content" name="app_content" class="mini-textarea" style="width:150px;height:80px;"></textarea>
                </td>
            </tr>
        </table>
        <div style="text-align: center; padding: 10px;">
            <a class="mini-button" onclick="cancel()" style="width: 60px; margin-right: 20px;"><fmt:message key="language.Cancel"/></a>
        </div>
    </div>
</div>
<script type="text/javascript">
    mini.parse();
    var grid = mini.get("userReplyGrid");
    grid.load();
    var detailWindow = mini.get("detailWindow");
    var detailform = new mini.Form("detailform");
    function onView(e) {
        var record = e.record;
        var tf_id = record.tf_id;
        var app_content = record.app_content;
        var s = '<a class="New_Button" href="javascript:showReplyDetails(\'' + record.app_name + '\',\''+app_content+'\')">' + app_content + '</a>';
        return s;
    }

    //显示反馈内容
    function showReplyDetails(app_name,app_content) {
    	detailWindow.show();
    	mini.get("app_name").setValue(app_name);
    	mini.get("app_content").setValue(app_content);
    }
    
    function cancel() {
        detailWindow.hide();
    }

    //搜索
    function search() {
        var a_contnet = mini.get("a_contnet").getFormValue();
        grid.load({
            "a_contnet": a_contnet
        });
    }

    //清空
    function reset() {
        mini.get("a_contnet").setValue("");
        grid.load({
        });
    }

</script>
</body>
</html>
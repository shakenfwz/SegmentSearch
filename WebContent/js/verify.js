function verify(){
    //alert("来了");
    //解决中文乱码问题的方法 1，页面端发出的数据做一次encodeURI，服务器端使用 new String(old.getBytes("iso8859-1"),"utf-8")
    //var url= "AJAXServer?name="+encodeURI($("#userName").val() ) ; // encodeURI处理中文乱码问题
   // 解决中文乱码问题的方法 2.页面端发出的数据做两次encodeURI处理， 服务器端用URLDecoder.decode(old,"utf-8");
    var input=$("input[name='type']");//因为得不到单选钮选定的value值，所以就只能先得到所有的值，通过循环来判断
    for(i=0;i<input.length;i++){
        if(input[i].checked){
            //alert(input[i].value);
            var url= "Users?name="+encodeURI(encodeURI($("#username").val() ))+"&password="+encodeURI(encodeURI($("#userpassword").val() ) )+"&type="+encodeURI(encodeURI(input[i].value) ); // encodeURI处理中文乱码问题
        }
    }
    url=convertURL(url);//获取函数的返回值'login?uname='+ uname + '&psw=' + psw
    $.get(url,null,function(data){
            $("#result1").html(data); //简洁版
    });
    //alert(url);
}
//给URL增加时间戳，骗过浏览器，不读取缓存
function convertURL(url){
    //获取时间戳
        var timstamp=(new Date()).valueOf();
    //将时间戳信息拼接到URL上
    if(url.indexOf("?")>=0){//用indexof判断该URL地址是否有问号
    url=url+"&t="+timstamp;
    }else{
       url=url+"?t="+timstamp;  
    }
   return  url;

}
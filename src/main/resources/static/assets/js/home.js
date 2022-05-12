/**
* home js
*/

// search result display
function onsearch(){
  window.location.href="search-result.html";
}

(function($) {
 to_pay();

})(jQuery);


function to_pay(){

$.ajax({
    url:"pay/toPay.html",
    type:"post",
    data: {detailsId: "1",price: 22 ,wayTrade: "PAGE" },
    dataType:"text",
    success:function(data){
        console.log("__成功__");
        $("body").append(data);

    },
    error:function(e){
        console.log("__失败__");
        console.log(e);


    }
})

}

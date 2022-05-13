/**
* home js
*/

// search result display
function onsearch(){
  window.location.href="search-result.html";
}

//(function($) {
// to_pay();
//
//})(jQuery);


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

function create_product(){
$.ajax({
    url:"api/v1/online/createProduct",
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


 $("#product-management-form").on("submit", function (e) {
     //阻止表单默认提交行为
     e.preventDefault();
     // 一次性获取表单的数据
     var formData =  {
        productCategory: $("#product_category").val(),
        productName: $("#product_name").val(),
        productDesc: $("#product_desc").val(),
        productPrice: $("#product_price").val(),
        productInventory: $("#product_inventory").val(),
        productDiscount: $("#product_discount").val(),
        SKU: $("#SKU").val()
     }


    $.post('api/v1/online/createProduct',formData, function(data) {
      //重置表单
               $("#product-management-form")[0].reset();
               // 刷新页面
               refresh();
    })
      .fail(function(data) {
        // place error code here
      });

//
//     $.ajax({
//       type: "POST",
//       url: "api/v1/online/createProduct",
//       data: fromdata,
//       dataType: "json",
//       dataType:"text",
//       success: function (res) {
//         //重置表单
//         $("#product-management-form")[0].reset();
//         // 刷新页面
//         refresh();
//       },
//     });
 });

 function refresh(){
 window.location.reload();
 }




/**
* home js
*/

// search result display
//function onsearch(){
//  window.location.href="search-result.html";
//}

//(function($) {
// to_pay();
//
//})(jQuery);

// 收银
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


// 产品配置
 $("#product-management-form").on("submit", function (e) {
     //阻止表单默认提交行为
     e.preventDefault();
     // 一次性获取表单的数据
     var formData =  {
        productCategory: $("#product_category").val(),
        productCode: $("#product_code").val(),
        productName: $("#product_name").val(),
        productDesc: $("#product_desc").val(),
        productPrice: $("#product_price").val(),
        productInventory: $("#product_inventory").val(),
        productDiscount: $("#product_discount").val(),
        SKU: $("#SKU").val(),
        featureImage: $("#featureImage").val(),
        productUrl: $("#productUrl").val()
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

 });
 // 产品配置修改
  $("#product-management-modify-form").on("submit", function (e) {
      //阻止表单默认提交行为
      e.preventDefault();
      // 一次性获取表单的数据
      var formData =  {
         productCategory: $("#modify_product_category").val(),
         productCode: $("#modify_product_code").val(),
         productName: $("#modify_product_name").val(),
         productDesc: $("#modify_product_desc").val(),
         productPrice: $("#modify_product_price").val(),
         productInventory: $("#modify_product_inventory").val(),
         productDiscount: $("#modify_product_discount").val(),
         SKU: $("#modify_SKU").val(),
         featureImage: $("#modify_featureImage").val(),
         productUrl: $("#modify_productUrl").val()
      }


     $.post('api/v1/online/modifyProduct',formData, function(data) {
       //重置表单
                $("#product-management-modify-form")[0].reset();
                // 刷新页面
                refresh();
     })
       .fail(function(data) {
         // place error code here
       });

  });


// 刷新
 function refresh(){
 window.location.reload();
 }
 // 产品定义
 function product_management_list(){
 $('#product-management-list').bootstrapTable('destroy');
// 产品列表
 $('#product-management-list').bootstrapTable({
   url: 'api/v1/online/productManagementList',
   method: 'post',
   pagination: true,
   search: true,
   dataField: 'rows',
   pageNumber : 1, // 分页当前
   pageSize: 10,   //每页的记录行数（*）
   paginationPreText: 'Pre', // 上一页
   paginationNextText: 'Next', // 下一页
   classes: 'table table-bordered table-hover table-striped',
   columns: [{
          field: 'category',
          title: 'product_category'
   }, {
         field: 'productCode',
         title: 'product_code'
   }, {
         field: 'productName',
         title: 'product_name'
   }, {
         field: 'price',
         title: 'product_price'
   }, {
         field: 'inventory',
         title: 'product_inventory'
   }, {
         field: 'discountPercent',
         title: 'discount_percent'
   }, {
         field: 'sku',
         title: 'SKU'
   }, {
         field: 'featureImage',
         title: 'feature_image'
   }, {
         field: 'productUrl',
         title: 'product_url'
   }, {
         field: 'createdAt',
         title: 'created_at'
   }, {
         field: 'modifiedAt',
         title: 'modified_at'
   },{
         field: 'productCode',
         title: 'action',
         formatter: productManagementActionFormatter
   }
   ],
   queryParams: function (params) {
               	// 这个函数主要是对请求参数的处理，发送js对象
                   var obj = {};
                   obj['size'] = $.isEmptyObject(params.offset) ? 15 : params.offset;
                   obj['current'] = $.isEmptyObject(params.limit) ? 1 : params.limit;

                   var temp = JSON.stringify(obj);
                   return JSON.parse(temp);
   },
   responseHandler: function (res) {
                   // 对返回参数进行处理
                   console.log(res);
                   return {
                       "total": res.data.total,
                       "rows": res.data.records
                   };
   },
   onLoadSuccess:function(data){
     // 添加点击事件

    // 修改商品配置 btn btn-primary modify-product
          $('.modify-product').on( 'click', function() {
                 var product = $(this).attr('data-product');
                 fillProductModifyForm(product);

                 // 产品编码禁止修改
                 $('#modify_product_code').attr("disabled",true);

             });

          //删除商品配置
           $('.delete-product').on( 'click', function() {
                  var productCode = $(this).attr('data-product-code');
                  onDeleteProduct(productCode);
              });
   }

 })
 }
// 产品相关操作
 function productManagementActionFormatter(value, row, index){
         var  formdata = {
             productCode: row.productCode,
             productDesc: row.productDesc,
             productName: row.productName,
             price: row.price,
             inventory: row.inventory,
             sku: row.sku,
             featureImage: row.featureImage,
             categoryId: row.categoryId,
             discountId: row.discountId,
             productUrl: row.productUrl

          };

    return  "<div class='dropdown'>"
             + "   <span  data-toggle='dropdown'>"
             +      "...."
             +    "</span>"
             +    "<div class='dropdown-menu'>"
             +     "<a href='product-preview.html?product_code="+row.productCode +"' title ='preview' class='preview-product dropdown-item'><i class='mdi mdi-eye'></i>preview</a>"
             +     "<a data-product-code='"+row.productCode +"' class='delete-product dropdown-item' title='delete'><i class='mdi mdi-delete '></i>delete</a>"
             +    "<a data-product='"+ JSON.stringify(formdata) +"' title ='modify' class='modify-product dropdown-item'><i class='mdi mdi-pencil'></i>modify</a>"
             +    "</div>"
             +  "</div>" ;
 }
// 商品分类配置
 $("#product-category-add-form").on("submit", function (e) {
     //阻止表单默认提交行为
     e.preventDefault();
     // 一次性获取表单的数据
     var formData =  {
        categoryName: $("#product_category_name").val(),
        categoryDesc: $("#product_category_desc").val()
     }


    $.post('api/v1/online/createCategory',formData, function(data) {
               //重置表单
               $("#product-category-add-form")[0].reset();
               // 刷新页面
               refresh();
    })
      .fail(function(data) {
        // place error code here
      });

 });
 // 商品分类
 function category_list(){
   $('#product-category-list').bootstrapTable('destroy');
   $('#product-category-list').bootstrapTable({
      url: 'api/v1/online/productCategoryList',
      method: 'post',
      pagination: true,
      dataField: 'rows',
      pageNumber : 1, // 分页当前
      pageSize: 10,   //每页的记录行数（*）
      paginationPreText: 'Pre', // 上一页
      paginationNextText: 'Next', // 下一页
      classes: 'table table-bordered table-hover table-striped',
      columns: [{
             field: 'categoryName',
             title: 'Category Name'
      }, {
            field: 'categoryDesc',
            title: 'Category Desc'
      }, {
            field: 'createdAt',
            title: 'created_at'
      }, {
            field: 'modifiedAt',
            title: 'modified_at'
      }
      ],
      queryParams: function (params) {
                  	// 这个函数主要是对请求参数的处理，发送js对象
                      var obj = {};
                      obj['size'] = $.isEmptyObject(params.offset) ? 15 : params.offset;
                      obj['current'] = $.isEmptyObject(params.limit) ? 1 : params.limit;
                      console.log(obj);
                      var temp = JSON.stringify(obj);
                      return JSON.parse(temp);
      },
      responseHandler: function (res) {
                      // 对返回参数进行处理
                      console.log(res);
                      return {
                          "total": res.data.total,
                          "rows": res.data.records
                      };
      },

    })
 }

 // 判断 product code 是否已被使用
 function checkProductCode(){
    var formData = {productCode: $("#product_code").val() }
    $.post('api/v1/online/checkProductCode',formData, function(data) {
            var registered = data.data;
            // 产品编码已使用 提示
            if(registered){
               $('#product-management-form .notice-warn').toggle();
            }

         })
           .fail(function(data) {
             // place error code here
           });
 }

// 优惠配置
  $("#discount-add-form").on("submit", function (e) {
      //阻止表单默认提交行为
      e.preventDefault();
      // 一次性获取表单的数据
      var formData =  {
         discountName: $("#discount_name").val(),
         discountDesc: $("#discount_desc").val(),
         discountPercent: Number($("#discount_percent").val()),
         active: Boolean(getRadioCheckedValueByName("discount_active"))
      }


     $.post('api/v1/online/createDiscount',formData, function(data) {
                //重置表单
                $("#discount-add-form")[0].reset();
                // 刷新页面
                refresh();
     })
       .fail(function(data) {
         // place error code here
       });

  });

  function getRadioCheckedValueByName(name){
  //通过名字获取  getElementsByName
  var obj = document.getElementsByName(name);
  //通过标签获取  getElementsByTagName
      for(var i=0; i<obj.length; i ++){
          if(obj[i].checked){
            return  obj[i].value
          }
      }
  }

// 优惠列表
 function discount_list(){
     $('#discount-list').bootstrapTable('destroy');
     $('#discount-list').bootstrapTable({
        url: 'api/v1/online/discountList',
        method: 'post',
        pagination: true,
        dataField: 'rows',
        pageNumber : 1, // 分页当前
        pageSize: 10,   //每页的记录行数（*）
        paginationPreText: 'Pre', // 上一页
        paginationNextText: 'Next', // 下一页
        classes: 'table table-bordered table-hover table-striped',
        columns: [{
               field: 'discountName',
               title: 'Discount Name'
        }, {
               field: 'discountDesc',
               title: 'Discount Desc'
        },{
               field: 'discountPercent',
               title: 'Discount Percent'
        }, {
               field: 'active',
               title: 'Active'
        }, {
               field: 'createdAt',
               title: 'created_at'
        }, {
               field: 'modifiedAt',
               title: 'modified_at'
        }
        ],
        queryParams: function (params) {
                    	// 这个函数主要是对请求参数的处理，发送js对象
                        var obj = {};
                        obj['size'] = $.isEmptyObject(params.offset) ? 15 : params.offset;
                        obj['current'] = $.isEmptyObject(params.limit) ? 1 : params.limit;
                        console.log(obj);
                        var temp = JSON.stringify(obj);
                        return JSON.parse(temp);
        },
        responseHandler: function (res) {
                        // 对返回参数进行处理
                        console.log(res);
                        return {
                            "total": res.data.total,
                            "rows": res.data.records
                        };
        },

      })
   }

// 商品目录下拉
function category_select(idSelector,selected){
    $.post('api/v1/online/category', function(data) {
           for(var i = 0 ; i< data.data.length;i++){
             $(idSelector).append("<option value='"+data.data[i].id+"'>"+data.data[i].categoryName+"</option>");
           }
           // 默认选中设置
           if( !$.isEmptyObject(selected) ){
              $(idSelector).val(selected);
           }


         })
           .fail(function(data) {
             // place error code here
           });
}

// 优惠下拉
function discount_select(idSelector,selected){
    $.post('api/v1/online/discount', function(data) {
           for(var i = 0 ; i< data.data.length;i++){
             $(idSelector).append("<option value='"+data.data[i].id+"'>"+data.data[i].discountName+"</option>");
           }
           // 默认选中设置
            if( !$.isEmptyObject(selected) ){
                         $(idSelector).val(selected);
            }
         })
           .fail(function(data) {
             // place error code here
           });
}
// 上传产品图片
 $(document).ready(function(){
    // 初始化
    // getProductInfo();

    var $input = $("#product-image-file-0a");
    $input.fileinput({
    uploadUrl: "api/v1/common/productImageFileUploadBatch", // 服务器接收上传文件的方法
    uploadAsync: true, // ajax
    showUpload: false, // 隐藏上传按钮
    showRemove: false, // 隐藏移除按钮
    uploadExtraData: function(){

                    var extraObject = {
                        productCode: getQueryVariable('product_code'),
                        images: JSON.stringify(preview_page.images)
                    };
                    return extraObject;
             }

    }).on("filebatchselected", function(event, files) {
    // 选择文件后立即触发上传方法
    $input.fileinput("upload");
    }).on('fileuploaded', function(event, data, previewId, index) {
          var uploadResult = data.response.data;

         uploadResult.map(function(item,index){
            var img ={ fileUrl: item.fileUrl,scaleFileUrl: item.scaleFileUrl };
            preview_page.images.push(img);
         });

      });

 });
 // 产品预览页
preview_page = new Vue({
    el:'#preview_page',
    data: {
      activeImageUrl: '',
      images: [],
      productIntroHtml: '',
      discountPrice: 33.00,
      price: 44.00
    },
    filters:{
      rounding(number){
       return number.toFixed(2);
      }
    }

 });


// 修改商品回显
function fillProductModifyForm(productJsonStr){
   var product = JSON.parse(productJsonStr);
   // 下拉
   category_select("#modify_product_category",product.categoryId);
   discount_select("#modify_product_discount",product.discountId);
       $("#modify_product_code").val(product.productCode);
       $("#modify_product_name").val(product.productName);
       $("#modify_product_desc").val(product.productDesc);
       $("#modify_product_price").val(product.price);
       $("#modify_product_inventory").val(product.inventory);
       $("#modify_SKU").val(product.sku);
       $("#modify_featureImage").val(product.featureImage);
       $("#modify_productUrl").val(product.productUrl);
   // 显示 form
   $('.section-product-modify').toggle();

    // 关闭list
   $('.section-product-list').toggle();
   $('.section-interaction').toggle();
   $('.interaction-out-spacer').toggle();

}


 // 删除商品配置
function onDeleteProduct(productCode){

   $.post('api/v1/online/deleteProduct',{productCode: productCode}, function(data) {

              refresh();
            })
              .fail(function(data) {
                // place error code here
              });
}
// 获取url 参数
 function getQueryVariable(variable){
   var query = window.location.search.substring(1);
          var vars = query.split("&");
          for (var i=0;i<vars.length;i++) {
                  var pair = vars[i].split("=");
                  if(pair[0] == variable){return pair[1];}
          }
          return(false);
 }
 // 产品预览数据
 function getProductInfo(){
 var formData =  {
          productCode: getQueryVariable('product_code')
      }
   $.post('api/v1/online/singleProduct',formData, function(data) {
              // 解析图片数据
              if(!$.isEmptyObject(data.data.product.images)){
                preview_page.images = JSON.parse(data.data.product.images);
                preview_page.activeImageUrl = getActiveImageUrl();
              }
              if(!$.isEmptyObject(data.data.product.productDesc)){
                preview_page.productIntroHtml = data.data.product.productDesc;
              }


            })
              .fail(function(data) {
                // place error code here
              });
 }
 // 产品图片上传
 function openImageUploadBrowser(){
    open('#product-image-file-0a');
 }
 // 打开文件浏览器
 function open(idSelector){
     $(idSelector).click();
 }
// 大图url
 function changeActiveImageUrl(scaleFileUrl){
  $(this).addClass('active');
  preview_page.activeImageUrl = getActiveImageUrl(scaleFileUrl);

 }

 function getActiveImageUrl(scaleFileUrl){
   for (var i=0;i<preview_page.images.length;i++) {
      if($.isEmptyObject(scaleFileUrl)){
        return preview_page.images[0].fileUrl
      }
      if(preview_page.images[i].scaleFileUrl== scaleFileUrl){return preview_page.images[i].fileUrl;}
   }

   return(false);

 }
// search product by productName
function getProductByName(){
   var productName = $("#search-product-input").val();
   if($.isEmptyObject(productName) ){
    return;
   }

   var opt = {
            query: {
               queryParams: productName
            }
          }

   $('#product-management-list').bootstrapTable('refresh',opt );

}





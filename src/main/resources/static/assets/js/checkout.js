// checkout page obj
var checkout_page = new Vue({
    el:'#checkout-page-content',
    data: {
      addressList: [],
      formShipping: {},
      orderSummary: {},
      showSavedAddress: true,
      formShippingAdd: {}
    },
    created(){
       checkout_page_onload();
    }
 });



 function getDiscountPrice(item){
     return item.price*(100-item.discountPercent)/100;
 }
 function getItemTotal(item){
     return item.quantity*item.price*(100-item.discountPercent)/100;
 }
 // load address list
 function loadAddresses(){
        $.post('/api/v1/selfService/address' ,function(data) {
           checkout_page.addressList = data.data;
           // address record is empty ,display none
           if($.isEmptyObject(data.data)){
             checkout_page.showSavedAddress  = false;
             return;
           }
           // default show first record
           showAddress(checkout_page.addressList[0]);
           checkout_page.formShipping.selectAddress = 0 ;
         })
           .fail(function(data) {
             // place error code here
           });
 }
 // refresh address
 function addressSelectOnchange(){
        var address = checkout_page.addressList[checkout_page.selectAddress];
         showAddress(address);
 }
 // bind shipping address
 function showAddress(address){
             checkout_page.formShipping.addressLine1 = address.addressLine1 ;
             checkout_page.formShipping.addressLine2 = address.addressLine2 ;
             checkout_page.formShipping.city         = address.city ;
             checkout_page.formShipping.postalCode   = address.postalCode ;
             checkout_page.formShipping.state        = address.state ;
             checkout_page.formShipping.country      = address.country ;
             checkout_page.formShipping.mobile       = address.mobile;
             checkout_page.formShipping.id       = address.id;
 }

 // load order summary
 function showOrderSummary(){
       $.post('api/v1/order/orderSummary' ,function(data) {
              checkout_page.orderSummary.items = data.data.items;
              checkout_page.orderSummary.subtotal = data.data.subtotal;
              checkout_page.orderSummary.tax = data.data.tax;
              checkout_page.orderSummary.shipping = data.data.shipping;
              checkout_page.orderSummary.total = data.data.total;
          })
            .fail(function(data) {
              // place error code here
            });
 }


function checkout_page_onload(){
           loadAddresses();
           showOrderSummary();
}

function addAddress(){

   $.post('api/v1/selfService/newAddress' ,checkout_page.formShippingAdd,function(data) {
                 $("#modal-add-address").modal('hide');
             })
               .fail(function(data) {
                 // place error code here
               });
}
function editAddress(){
 $.post('api/v1/selfService/modifyAddress' ,checkout_page.formShipping, function(data) {
             $("#modal-modify-address").modal('hide');
           })
             .fail(function(data) {
               // place error code here
             });
}

function delAddress(){
var address = checkout_page.addressList[checkout_page.formShipping.selectAddress];

$.post('api/v1/selfService/removeAddress' ,{id: address.id},function(data) {
           $("#modal-modify-address").modal('hide');
          })
            .fail(function(data) {
              // place error code here
            });
}

function toPay(){
// new order
var address = checkout_page.addressList[checkout_page.formShipping.selectAddress];
$.post('api/v1/order/newOrder' ,{addressId: address.id},function(data) {
          if(!!data.data){
              // go pay page
              var orderDetailsId = data.data;
              window.location.href= 'payPalV2/toPay.html?orderDetailsId=' + orderDetailsId;
          }
})
            .fail(function(data) {
              // place error code here
            });

}

function toPayPage(orderDetailsId){
  var form  = $("<form method = 'post'></form>");
  var url = 'payPalV2/toPay.html';
  form.attr({"action": toPay.html});
  var input = $("<input type='hidden' name='orderDetailsId' value="+orderDetailsId+">");
  form.append(input);
  form.submit;
}

   var current_fs, next_fs, previous_fs; //fieldsets
    var opacity;
function progressbarNext(e){
 current_fs = $(e.target).parent();
     next_fs = $(e.target).parent().next();

     //Add Class Active
     $("#progressbar li").eq($("fieldset").index(next_fs)).addClass("active");

     //show the next fieldset
     next_fs.show();
     //hide the current fieldset with style
     current_fs.animate({opacity: 0}, {
         step: function(now) {
             // for making fielset appear animation
             opacity = 1 - now;

             current_fs.css({
                 'display': 'none',
                 'position': 'relative'
             });
             next_fs.css({'opacity': opacity});
         },
         duration: 600
     });
}
function progressbarPrevious(e){
    current_fs = $(e.target).parent();
    previous_fs = $(e.target).parent().prev();

    //Remove class active
    $("#progressbar li").eq($("fieldset").index(current_fs)).removeClass("active");

    //show the previous fieldset
    previous_fs.show();

    //hide the current fieldset with style
    current_fs.animate({opacity: 0}, {
        step: function(now) {
            // for making fielset appear animation
            opacity = 1 - now;

            current_fs.css({
                'display': 'none',
                'position': 'relative'
            });
            previous_fs.css({'opacity': opacity});
        },
        duration: 600
    });
}


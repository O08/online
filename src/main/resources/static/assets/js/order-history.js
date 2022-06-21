var orderHistory_page = new Vue({
    el:'#page',
    data: {
      showSearchWrap: false,
      orderHistoryList: []
    }
 });
loadOrders();
function loadOrders(){
  $.post('api/v1/order/orderHistory',function(data) {
               var orders = data.data;
               orderHistory_page.orderHistoryList = orders;

              })
                .fail(function(data) {
                  // place error code here
                });
}

 function toggleSearch(){
     orderHistory_page.showSearchWrap = !orderHistory_page.showSearchWrap;
  }

/**
* online sale js
*/
//
var cartItemTemplate = '<tr class="cart-item"><td class="product-thumbnail"><a href="#{productUrl}"><img width="170"height="240"src="#{featuredImageUrl}"alt=""class="attachment-shop_thumbnail"/></a></td><td class="product-name"><h4><a href="#{productUrl}">#{productName}</a></h4></td><td class="product-price"data-title="Price:"><span class="amount">$#{price}</span></td><td class="product-quantity"data-title="Quantity:"><div class="quantity"><select class="select-style cart-select"data-product="#{productCode}"><option value="1">1</option><option value="2">2</option><option value="3">3</option></select></div></td><td class="product-remove"><button class="remove"data-product="#{productCode}"><i class="flaticon-waste-can"></i><span class="remove__text">Remove</span></button></td><td class="product-subtotal"data-title="Total:"><span class="amount">$#{total}</span></td></tr>';

var miniCartItemTemplate = '<li class="mini-cart-item"><div class="mini-cart-item__thumb"><a href="#{productUrl}"><img src="#{featuredImageUrl}"alt=""></a></div><div class="mini-cart-item__content"><div class="mini-cart-item__title"><a href="#{productUrl}">#{productName}</a></div><span class="amount">$#{price}</span></div></li>';

var miniCartTemplate = '<div class="mini-cart__header">Your cart</div><div class="mini-cart__content"><ul class="mini-cart-list">#{miniCartList}</ul><div class="mini-cart__subtotal d-flex flex-wrap justify-content-between"><span>Subtotal</span><span>#{subtotal}</span></div><a href="/cart.html"class="btn btn-primary btn-lg btn-block mini-cart__btn">checkout</a></div><div class="mini-cart__footer"><div class="icon-box icon-box-left justify-content-center"><div class="icon-box__icon"><i class="flaticon flaticon-delivery-truck"></i></div><div class="icon-box__title">Delivery to all regions</div></div></div>';

function getTemplate(keySelector){
   return $(keySelector).html();
}

// reload
 function refresh(){
 window.location.reload();
 }

// home display special product
function showFeaturedProducts(){

$.post('/api/v1/online/featuredProducts', function(data) {

               var featuredProductsHtml ='';
               var products = data.data;
                for (var i=0;i<products.length;i++) {

                    featuredProductsHtml =  featuredProductsHtml + getProductCardHtml(products[i]);
                }
                $('.featured-products').html(featuredProductsHtml);

            })
              .fail(function(data) {
                // place error code here
              });
}
function getProductCardHtml(product){
                    var productHtml ='';
                    if(product.discountPercent > 0){
                      var discountPrice = product.price * (100-product.discountPercent)/100;
                     productHtml = getTemplate('#featuredProductCard-template-with-discount')
                         .replaceAll('#{discount}','-'+product.discountPercent+'%')
                         .replaceAll('#{discountPrice}',discountPrice.toFixed(2));
                    }
                    if(product.discountPercent === 0){
                       productHtml = getTemplate('#featuredProductCard-template-No-discount');
                    }
                    productHtml = productHtml.replaceAll('#{productUrl}',product.productUrl)
                    .replaceAll('#{featuredImageUrl}',product.featureImage)
                    .replaceAll('#{productName}',product.productName).replaceAll('#{price}',product.price.toFixed(2));

                    return productHtml;

}
// subscribe newsletter
$("#subscribe-form").on( 'submit', function(e) {
 // prevent default submit
    e.preventDefault();

             var email = $('.subscribe-form__input').val();
               var formData = {
                 email: email
               }

              $.post('/api/v1/online/subscribeNewsLetter', formData ,function(data) {
                           window.location.href="/";
                          })
                            .fail(function(data) {
                              // place error code here
                              alert('Wooho,Something wrong!');
                            });
          });


function loadOnlineProducts(current){
           var category = getQueryVariable('category');

           var queryParams = {
               orderBy: $('#order_by').val(),
               category: !category ? '' : category
           }

           var formData = {
                     current: current,
                     size: $('#per_page').val(),
                     queryParams: JSON.stringify(queryParams)
           }
            $.ajax({
                url: "/api/v1/online/onlineProductList",
                data: JSON.stringify(formData),
                type: "post",
                dataType:"json",
                contentType: "application/json",
                success:function(data){
                  var products = data.data.records;
                  totalpage = data.data.pages;
                  var currentPage = data.data.current;
                  var shopProductsHtml = '';
                  for(var i in products){
                     shopProductsHtml = shopProductsHtml + getProductCardHtml(products[i]);
                  }
                  $('#shop-products').html(shopProductsHtml);
                  var pagerHtml = pager(totalpage,currentPage);
                  $('#pager').html(pagerHtml);
                },
                error:function(){
                  //alert('error'); //错误的处理
                }
              });
}

function prevpage(prev){

   if(prev == 1){
     return ;
   }
   loadOnlineProducts(prev -1 );
}

var totalpage = 0;
function nextpage(next){
   if(totalpage == next){
     return ;
   }
   loadOnlineProducts(next+1);

}
// pager html
function pager(pages,current){
var pagerHtml = '';
var prevHtml = '<li>'
              + '<a href=\"javascript:prevpage('+ current + ')\"  aria-label=\"Previous\">'
              + '<span aria-hidden=\"true\"><i class=\"flaticon-left-arrow-sign\"></i> Previous</span>'
              + '</a>'
              + '</li>';
var nextHtml = '<li>'
               + ' <a href=\"javascript:nextpage('+ current + ')\"  aria-label=\"Next\">'
               + '<span aria-hidden=\"true\">Next <i class=\"flaticon-right-direction\"></i></span>'
               + '</a>'
               + '</li>';

pagerHtml = prevHtml + pagination(pages,current) +   nextHtml;

return pagerHtml;
}
// pages: total page
// current: show page
function pagination(pages,current){
 var paginationHtml = '';
 var interval = pages - current;
 var left = 0;
 var right = 0;
 var omitHtml = '';
 if(interval <= 5){
   right = pages-1;
   left = pages  > 6 ? pages - 5 : 1;
 }
 if(interval > 5){
   left = current - 1 > 1 ? current -1 : 1;
   right = left +4;
   omitHtml = '<li><a href=\"#\">...</a></li>' ;
 }
  for(var page =left ; page<= right; page++){
      paginationHtml = paginationHtml + '<li><a href=\"javascript:gopage('+ page + ')\">'+ page + '</a></li>';
  }
  paginationHtml = paginationHtml + omitHtml + '<li><a href=\"#\">'+ pages + '</a></li>';

  return paginationHtml;
}
// go page
function gopage(page){
 loadOnlineProducts(page);
}
// shop.html page params change trigger
$("#per_page").on( 'change', function() {
             loadOnlineProducts(1);
          });

// shop.html  order params change trigger
$("#order_by").on( 'change', function() {
             loadOnlineProducts(1);
          });
// contact us
$(".contact-form").on( 'submit', function(e) {
// 阻止默认提交
             e.preventDefault();
             var firstname = $('#contact-firstname').val();
             var lastname = $('#contact-lastname').val();
             var email = $('#contact-email').val();
             var phone = $('#contact-phone').val();
             var message = $('#contact-message').val();
             var formData = {
                firstName: firstname,
                lastName: lastname,
                email: email,
                phoneNumber: phone,
                message: message
              }

              	$.post('/api/v1/online/contactus',formData, function(data) {

                               // 刷新页面
                               refresh();
                    })
                      .fail(function(data) {
                        // place error code here
                        // place error code here
                                        alert('Wooho,Something wrong!')
                      });

          });
// recommend
function recommendProducts(){

$.post('/api/v1/online/recommendProducts', function(data) {

               var recommendProductsHtml ='';
               var products = data.data;
                for (var i=0;i<products.length;i++) {

                    recommendProductsHtml =  recommendProductsHtml + getProductCardHtml(products[i]);
                }
                $('.featured-products').html(recommendProductsHtml);

            })
              .fail(function(data) {
                // place error code here
              });

}
// auth
function getPublicKeyInfo(){
    var publicKeyInfo = '';
    $.ajax({
			url : '/api/v1/auth/getPublicKey',
			type : 'POST',
			async: false,
			data : {},
			dataType : 'json',
			success : function(result) {
				publicKeyInfo = result.data;
			}
	});
	return publicKeyInfo;
}


function checkPasswords(){

	var password = $("#signup-password").val();
	var confirmedPassword = $("#signup-confirmed-password").val();
    var note = document.querySelector('#signup-confirmed-password');

     password != confirmedPassword ? note.setCustomValidity("Passwords do not match, please retype")
      : note.setCustomValidity("");
      note.reportValidity();
}

function doSignUp(){

	    var email = $("#signup-email").val();
	    var fullname = $("#signup-full-name").val();
	    var password = $("#signup-password").val();

	    var publicKeyInfo = getPublicKeyInfo();

    	var encrypt = new JSEncrypt();// plugin encrypt
        encrypt.setPublicKey(publicKeyInfo.publicKey);// set publicKey
        var encryptedPassword = encrypt.encrypt(password );// encrypt password

        var signupInfo = {
          email: email,
          fullname: fullname,
          password: encryptedPassword,
          time: publicKeyInfo.time
        };

	$.ajax({
		url : '/api/v1/auth/doSignUp',
		type : 'POST',
		data : signupInfo ,
		dataType : 'json',
		success : function(result) {
		    //  reset form
		    $("#signup-form")[0].reset();
             if(result.data){
               // to login
               $(".login").toggle();
               $(".signup").toggle();
             }
             if(!result.data){
               // alert fail
               alert('Whoops! Your Signup has failed');
             }
		},
		error : function() {
			// alert fail
			alert('Whoops! Your Signup has failed');
		}
	});
}

$("#signup-form").on("submit", function (e) {

    // prevent default submit
    e.preventDefault();
     doSignUp();

  });
$("#login-form").on("submit", function (e) {
     // encrypted password
    var  passwordEncrypted = rsaEncrypt($("#login-password").val()) ;
    $("#login-password").val(passwordEncrypted);

  });

// hidden login form show sign up form
$(".sign-up-link").on("click", function (e) {
   // reset login form
   $("#login-form")[0].reset();
   $(".login").toggle();
  $(".signup").toggle();

  });

// hidden sign up form show login form
$(".sign-in-link").on("click", function (e) {
   // reset sign up form
   $("#signup-form")[0].reset();
   $(".login").toggle();
    $(".signup").toggle();
  });

function rsaEncrypt(data){

        var publicKeyInfo = getPublicKeyInfo();
    	var encrypt = new JSEncrypt();// plugin encrypt
        encrypt.setPublicKey(publicKeyInfo.publicKey);// set publicKey
        var encrypted = encrypt.encrypt(data);// encrypt
        return encrypted;

}

function addToCart(){
// if user not yet login . to login
var alreadyLogin = $(".nav-account-container").is(":visible");
if(!alreadyLogin){
  window.location.href = "/authentication.html";
  return;
}
  var productCode = getQueryVariable('productCode');
  var quantity = $('#quantity').val();
  if($.isEmptyObject(productCode) ||  $.isEmptyObject(quantity) ){
    return;
  }
  var formData = {
    productCode: productCode,
    quantity: quantity
  };
$.post('/api/v1/shop/addCartItem',formData, function(data) {
                  if(data.code == 2001){
                      alert(data.desc);
                      return;
                  }
                  if(data.data){
                    window.location.href='/shop.html';
                    return ;
                  }

                  if(!data.data){
                    alert('Wooho,Something wrong!')
                  }

            })
              .fail(function(data) {
                // place error code here
                alert('Wooho,Something wrong!')
              });
}
//calculate cart  subtotal
function calculateSubtotal(cartItems){
     var subtotal = 0;
     for(var i in cartItems){
     var discountPrice = cartItems[i].price*(100-cartItems[i].discountPercent)/100;
       subtotal = subtotal + discountPrice*cartItems[i].quantity;
     }
     return !subtotal ? '0.00' : subtotal.toFixed(2);
}
function showCartItem(){

$.post('/api/v1/shop/cart', function(data) {


                  var cartItems = data.data;
                  var cartItemHtml = '';
                  for(var i in cartItems){
                    cartItemHtml = cartItemHtml + getCartItemCardHtml(cartItems[i]);
                  }
                  // show cart item
                  $('#cart-table tbody').html(cartItemHtml);
                  // show cart subtotal
                  $('#cart-subtotal').html('$'+calculateSubtotal(cartItems));
                  // show mini cart
                  showMiniCart();

                  // binding event
                  $(".remove").on("click", function (e) {
                     var productCode =  $(this).attr('data-product');
                      removeItemFromCart(productCode);
                    });

                   $('.cart-select').on("change",function(e){
                     var productCode =  $(this).attr('data-product');
                     var quantity =  $(this).val();
                     updateCartItem(productCode,quantity);
                   });

            })
              .fail(function(data) {
                // place error code here
                alert('Wooho,Something wrong!')
              });
}

function updateCartItem(productCode,quantity){
var formData = {
   productCode: productCode,
   quantity: quantity
}
$.post('/api/v1/shop/updateCartItem',formData, function(data) {

                  if(data.data){
                    showCartItem();
                  }
                  if(!data.data){
                    alert('Wooho,Something wrong!')
                  }

            })
              .fail(function(data) {
                // place error code here
                alert('Wooho,Something wrong!')
              });

}

function removeItemFromCart(productCode){
$.post('/api/v1/shop/delCartItem',{productCode: productCode}, function(data) {

                  if(data.data){
                    showCartItem();
                  }
                  if(!data.data){
                    alert('Wooho,Something wrong!')
                  }

            })
              .fail(function(data) {
                // place error code here
                alert('Wooho,Something wrong!')
              });

}
function getCartItemCardHtml(cartItem){
                    var cartItemHtml ='';

                    cartItemHtml = cartItemTemplate;
                    // replace template
                    var discountPrice = cartItem.price*(100-cartItem.discountPercent)/100;
                    cartItemHtml = cartItemHtml.replaceAll('#{productUrl}',cartItem.productUrl)
                    .replaceAll('#{featuredImageUrl}',cartItem.featureImage)
                    .replaceAll('#{productName}',cartItem.productName).replaceAll('#{price}',discountPrice.toFixed(2))
                    .replaceAll('>'+cartItem.quantity+'</option>',' selected = "selected">' + cartItem.quantity +'</option>')
                    .replaceAll('#{productCode}',cartItem.productCode)
                    .replaceAll('#{total}',(discountPrice*cartItem.quantity).toFixed(2))
                    ;
                    return cartItemHtml;

}

$(".add_to_cart_button").on("click", function (e) {
    addToCart();
  });



// get url param
 function getQueryVariable(variable){
   var query = window.location.search.substring(1);
          var vars = query.split("&");
          for (var i=0;i<vars.length;i++) {
                  var pair = vars[i].split("=");
                  if(pair[0] == variable){return pair[1];}
          }
          return(false);
 }

function showSearchProducts(){
 var keyword = getQueryVariable('keyword');
 if(!keyword){
   return;
 }
$.post('/api/v1/online/search',{keyword: keyword } ,function(data) {

               var searchedProductsHtml ='';
               var products = data.data;
                for (var i=0;i<products.length;i++) {

                    searchedProductsHtml =  searchedProductsHtml + getProductCardHtml(products[i]);
                }
                $('#search-result-ul').html(searchedProductsHtml);

            })
              .fail(function(data) {
                // place error code here
              });
}
// display miniCart
showMiniCart();
function showMiniCart(){
$.post('/api/v1/shop/cart', function(data) {


                  var cartItems = data.data;
                  var miniCartListHtml = '';
                  var miniCartHtml = '';
                  if($.isEmptyObject(cartItems) ){
                    var noCartItemHtml = '<a href="/cart.html"class="mini-cart-link"><i class="cart__icon flaticon-online-shopping-cart fi-2x"></i><span class="mini-cart-link__qty">0 items</span></a><div class="mini-cart"><div class="mini-cart__content"><div class="empty-message">You have no items in your cart</div><a href="shop.html"class="btn btn-primary btn-lg mini-cart__btn">shop more</a></div><div class="mini-cart__footer"><div class="icon-box icon-box-left justify-content-center"><div class="icon-box__icon"><i class="flaticon flaticon-delivery-truck"></i></div><div class="icon-box__title">Delivery to all regions</div></div></div></div>';
                    $('.header-mini-cart').html(noCartItemHtml);
                    // hidden checkout button
                    $('#cart-checkout').hide();
                    return;
                  }

                  // minicartitem html
                   for (var i=0;i<cartItems.length;i++) {
                      var discountPrice = cartItems[i].price*(100-cartItems[i].discountPercent)/100;
                      var price = !discountPrice ? '' : (discountPrice*cartItems[i].quantity).toFixed(2);
                      miniCartListHtml = miniCartListHtml + miniCartItemTemplate.replaceAll('#{productUrl}',cartItems[i].productUrl)
                                          .replaceAll('#{featuredImageUrl}',cartItems[i].featureImage)
                                          .replaceAll('#{productName}',cartItems[i].productName)
                                          .replaceAll('#{price}',price);
                   }

                  var subtotal = calculateSubtotal(cartItems);

                  miniCartHtml = miniCartTemplate.replaceAll('#{miniCartList}',miniCartListHtml)
                                                  .replaceAll('#{subtotal}','$'+subtotal);
                   // show mini cart quality
                   $('.mini-cart-link__qty').html(cartItems.length+' items');
                   $('.mini-cart').html(miniCartHtml);
            })
              .fail(function(data) {
                // place error code here
                alert('Wooho,Something wrong!');
              });
}
// user basic status
loginDetect();
function loginDetect(){
  // display none my account
  $('.nav-account-container').toggle();
  $.post('/api/v1/nav/customer',function(data) {
               var customer = data.data;
               if(!!customer && !!customer.username){
                 $('.nav-sign-up').toggle();
                 $('.nav-sign-in').toggle();
                 $('.nav-account-container').toggle();
               }
              })
                .fail(function(data) {
                  // place error code here
                  alert('Wooho,Something wrong!');
                });
}
// call api load category list
function shopCategories(){

  $.post('/api/v1/online/shopCategories',{},function(data) {
               if(data.data){
                 var categories = data.data;
                 var categoryHtml = '<li class="nav-item"><a href="/shop.html" class="nav-link active">All</a></li>';
                 for (var i=0;i<categories.length;i++) {
                    categoryHtml = categoryHtml + '<li class="nav-item"><a href="/shop.html?category='+categories[i].id+'" class="nav-link">'+categories[i].categoryName+'</a></li>';
                  }
                  $('.shop-categories-nav ul').html(categoryHtml);
               }

              })
                .fail(function(data) {
                  // place error code here
                  alert('Wooho,Something wrong!');
                });
}

 // 产品预览数据
 function getProductInfo(productCode){
 var formData =  {
          productCode: productCode
      }
   $.post('/api/v1/online/singleProduct',formData, function(data) {
             var product = data.data.product;
             if(product.discountPercent>0){
               var discountPrice = product.price*(100-product.discountPercent)/100;
               var priceInfo = '<ins><span class="amount">$'+discountPrice.toFixed(2) +'</span></ins><del><span class="amount">'+product.price.toFixed(2)+'</span></del>';
               $('.product-summary .price').html(priceInfo);
               var discountPriceInfo = '<span class="onsale">-'+product.discountPercent+'%</span>';
               $('.product-gallery').before(discountPriceInfo);
               return;
             }

             $('.product-summary .price').html('<span class="amount">$'+product.price.toFixed(2)+'</span>');


            })
              .fail(function(data) {
                // place error code here
                alert('Wooho,Something wrong!');
              });
 }


/**
* online sale js
*/
//
var cartItemTemplate = '<tr class="cart-item"><td class="product-thumbnail"><a href="#{productUrl}"><img width="170"height="240"src="#{featuredImageUrl}"alt=""class="attachment-shop_thumbnail"/></a></td><td class="product-name"><h4><a href="#{productUrl}">#{productName}</a></h4></td><td class="product-price"data-title="Price:"><span class="amount">$#{price}</span></td><td class="product-quantity"data-title="Quantity:"><div class="quantity"><select class="select-style cart-select"data-product="#{productCode}"><option value="1">1</option><option value="2">2</option><option value="3">3</option></select></div></td><td class="product-remove"><button class="remove"data-product="#{productCode}"><i class="flaticon-waste-can"></i><span class="remove__text">Remove</span></button></td><td class="product-subtotal"data-title="Total:"><span class="amount">$#{total}</span></td></tr>'

function getTemplate(keySelector){
   return $(keySelector).html();
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
                     productHtml = getTemplate('#featuredProductCard-template-with-discount')
                         .replace('#{discount}','-'+product.discountPercent+'%')
                         .replace('#{discountPrice}',product.price * product.discountPercent /100);
                    }
                    if(product.discountPercent === 0){
                       productHtml = getTemplate('#featuredProductCard-template-No-discount');
                    }
                    productHtml = productHtml.replace('#{productUrl}',product.productUrl)
                    .replace('#{featuredImageUrl}',product.featureImage)
                    .replace('#{productName}',product.productName).replace('#{price}',product.price);

                    return productHtml;

}
// subscribe newsletter
$(".subscribe-form__btn").on( 'click', function() {
             var email = $('.subscribe-form__input').val();
             if($.isEmptyObject(email)){
                return;
             }
               var formData = {
                 email: email
               }

              $.post('/api/v1/online/subscribeNewsLetter', formData ,function(data) {
                          })
                            .fail(function(data) {
                              // place error code here
                            });
          });


function loadSingleProducts(current){
           var formData = {
                     current: current,
                     size: $('#per_page').val(),
                     queryParams: $('#order_by').val()
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
   loadSingleProducts(prev -1 );
}

var totalpage = 0;
function nextpage(next){
   if(totalpage == next){
     return ;
   }
   loadSingleProducts(next+1);

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
 loadSingleProducts(page);
}
// shop.html page params change trigger
$("#per_page").on( 'change', function() {
             loadSingleProducts(1);
          });

// shop.html  order params change trigger
$("#order_by").on( 'change', function() {
             loadSingleProducts(1);
          });
// contact us
$(".contact-form__btn").on( 'click', function() {
             var firstname = $('#contact-firstname').val();
             var lastname = $('#contact-lastname').val();
             var email = $('#contact-email').val();
             var phone = $('#contact-phone').val();
             var message = $('#contact-message').val();
             if($.isEmptyObject(message)){
                return;
             }
             var formData = {
                firstname: firstname,
                lastname: lastname,
                email: email,
                phone: phone,
                message: message
              }

              $.post('/api/v1/online/contactus', formData ,function(data) {
                          })
                            .fail(function(data) {
                              // place error code here
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
			url : 'api/v1/auth/getPublicKey',
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

     if(password != confirmedPassword){
       $("#signup-password").setCustomValidity("Passwords do not match, please retype");
     }
}

function doSignUp(){

	    var email = $("#signup-email").val();
	    var fullname = $("#signup-full-name").val();
	    var password = $("#signup-password").val();

	    var publicKeyInfo = getPublicKeyInfo();
        var signupInfo = {
          email: email,
          fullname: fullname,
          password: password,
          time: publicKeyInfo.time
        };

    	var encrypt = new JSEncrypt();// plugin encrypt
        encrypt.setPublicKey(publicKeyInfo.publicKey);// set publicKey
        var encrypted = encrypt.encrypt(JSON.stringify(signupInfo) );// encrypt sign up info

	$.ajax({
		url : 'api/v1/auth/doSignUp',
		type : 'POST',
		data : {
		encrypted: encrypted
		},
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
  var productCode = getQueryVariable('productCode');
  var quantity = $('#quantity').val();
  if($.isEmptyObject(productCode) ||  $.isEmptyObject(quantity) ){
    return;
  }
  var formData = {
    productCode: productCode,
    quantity: quantity
  };
$.post('api/v1/shop/addCartItem',formData, function(data) {

                  if(data.data){
                    window.location.href='shop.html';
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
     return subtotal;
}
function showCartItem(){

$.post('api/v1/shop/cart', function(data) {


                  var cartItems = data.data;
                  var cartItemHtml = '';
                  for(var i in cartItems){
                    cartItemHtml = cartItemHtml + getCartItemCardHtml(cartItems[i]);
                  }
                  // show cart item
                  $('#cart-table tbody').html(cartItemHtml);
                  // show cart subtotal
                  $('#cart-subtotal').html('$'+calculateSubtotal(cartItems));
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
$.post('api/v1/shop/updateCartItem',formData, function(data) {

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
$.post('api/v1/shop/delCartItem',{productCode: productCode}, function(data) {

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
                    .replaceAll('#{productName}',cartItem.productName).replaceAll('#{price}',discountPrice)
                    .replaceAll('>'+cartItem.quantity+'</option>',' selected = "selected">' + cartItem.quantity +'</option>')
                    .replaceAll('#{productCode}',cartItem.productCode)
                    .replaceAll('#{total}',discountPrice*cartItem.quantity)
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

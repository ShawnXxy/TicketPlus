(function() {

    /************
    * Variables
    *************/
    var user_id = '';
    var user_fullname = '';
    // SF
    var lng = -122.08;
    var lat = 37.38;

    /*******************
    *   INITIALIZING
    *******************/
    function init() {
        // Register event
        // $()
        $('login-btn').addEventListener('click', login); // login() defined at LOGIN
        $('nearby-btn').addEventListener('click', loadNearbyItems); // loadNearbyItems() defined at EVENTS/ITEMS SETTING
        $('fav-btn').addEventListener('click', loadFavoriteItems); // loadFavoriteItems() defined at EVENTS/ITEMS SETTING
        $('recommend-btn').addEventListener('click', loadRecommendedItems); // loadRecommendedItems() defined at EVENTS/ITEMS SETTING

        validateSession();
        // create a fake user to test
        // onSessionValid({
        //     user_id: '1111',
        //     name: 'Xiangyu Xiao'
        // }); // onSessionValid() defined at SESSION
    } // End of init()

    /*******************
	* SESSION SETTINGS
	*********************/
    function validateSession() {
        // The request parameters
        var url = './LoginServlet';
        var req = JSON.stringify({});

        // display loading message
        showLoadingMessage('Validating session...'); // showLoadingMessage() defined at DISPLAY SETTING

        // Make AJAX call
        ajax('GET', url, req,
            // Session is still valid
            function(res) {
                var result = JSON.parse(res);
                if (result.status === 'OK') {
                    onSessionValid(result);
                }
        }); //
    } // End of validateSession()

    function onSessionValid(result) {
        user_id = result.user_id;
        user_fullname = result.name;

        var loginForm = $('login-form');
        var itemNav = $('item-nav');
        var itemList = $('item-list');
        var avatar = $('avatar');
        var welcomeMsg = $('welcome-msg');
        var logoutBtn = $('logout-link');

        welcomeMsg.innerHTML = 'Welcome, ' + user_fullname;

        // showElement()
        showElement(itemNav);
        showElement(itemList);
        showElement(avatar);
        showElement(welcomeMsg);
        showElement(logoutBtn, 'inline-block');
        // hideElement()
        hideElement(loginForm);

        initGeoLocation(); // defined at GEOLOCATION
    } // End of onSessionValid()

    function onSessionInvalid() {
        var loginForm = $('login-form');
        var itemNav = $('item-nav');
        var itemList = $('item-list');
        var avatar = $('avatar');
        var welcomeMsg = $('welcome-msg');
        var logoutBtn = $('logout-link');

        // Hide views if failed login
        hideElement(itemNav);
        hideElement(itemList);
        hideElement(avatar);
        hideElement(welcomeMsg);
        hideElement(logoutBtn);
        // hideElement()
        showElement(loginForm);

    } // End of onSessionInvalid()

    /*****************************
	* GEOLOCATION SETTINGS
	*******************************/
    function initGeoLocation() {
        // https://dev.w3.org/geo/api/spec-source.html
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(onPositionUpdated,
                onLoadPositionFailed,
                    {
                        maximumAge : 60000
                    } // By using the 'maximumAge' option above, the position object is guaranteed to be at most 10 minutes old.
            );
            showLoadingMessage('Retrieving your location...'); // showLoadingMessage() defined at DISPLAY SETTING
        } else {
            onLoadPositionFailed(); // defined below
        }
    } // End of initGeoLocation()

    function onPositionUpdated(position) {
        lat = position.coords.latitude;
        lng = position.coords.longitude;

        loadNearbyItems(); // loadRecommendedItems() defined at EVENTS/ITEMS SETTING
    }

    function onLoadPositionFailed() {
        console.warn('navigator.geolocation is not available!');
        getLocationFromIP(); // defined below
    }

    function getLocationFromIP() {
        // Get location from http://ipinfo.io/json
        var url = 'http://ipinfo.io/json';
        var req = null;

        ajax('GET', url, req,
            function(res) {
                var result = JSON.parse(res);
                if ('loc' in result) {
                    var loc = result.loc.split(',');
                    lat = loc[0];
                    lng = loc[1];
                } else {
                    console.warn('Getting location by IP failed!');
                }
                loadNearbyItems(); // loadRecommendedItems() defined at EVENTS/ITEMS SETTING
            }
        ); //
    } // End of getLocationFromIP()

    /********************
	* LOGIN SETTINGS
	**********************/
    function login() {
        var username = $('username').value;
        var password = $('password').value;
        // MD5 hash
        password = md5(username + md5(password));

        // The request parameters
        var url = './LoginServlet';
        var params = 'user_id=' + username + '&password=' + password;
        var req = JSON.stringify({});

        ajax('POST', url + '?' + params, req,
            // Successful callback
            function(res) {
                var result = JSON.parse(res);
                // Successful logged in
                if (result.status === 'OK') {
                    onSessionValid(result);
                }
            },
            // with error handler
            function() {
                showLoginError(); // defined below
            }
        ); //
    } // End of login()

    function showLoginError() {
        $('login-error').innerHTML = 'Invalid username or password';
    }

    function clearLoginError() {
        $('login-error').innerHTML = '';
    }

    /********************
	* DISPLAY SETTINGS
	**********************/
    function activeBtn(btnId) {
        var btns = document.getElementsByClassName('main-nav-btn');

        // de-active all navigation buttons
        for (var i = 0; i < btns.length; i++) {
            btns[i].className = btns[i].className.replace(/\bactive\b/, '');
        }

        // active the one that has id = btnId
        var btn = $(btnId);
        btn.className += ' active';
    } // End of activeBtn()

    function showLoadingMessage(msg) {
        var itemList = $('item-list');
        itemList.innerHTML = '<p class="notice"><i class="fa fa-spinner fa-spin"></i> ' + msg + '</p>';
    }

    function showWarningMessage(msg) {
        var itemList = $('item-list');
        itemList.innerHTML = '<p class="notice"><i class="fa fa-exclamation-triangle"></i> ' + msg + '</p>';
    }

    function showErrorMessage(msg) {
        var itemList = $('item-list');
        itemList.innerHTML = '<p class="notice"><i class="fa fa-exclamation-circle"></i> ' + msg + '</p>';
    }

    function hideElement(element) {
        element.style.display = 'none';
    }

    function showElement(element, style) {
        // var displayStyle;
        // if (style) {
        //     displayStyle = style;
        // } else {
        //     displayStyle = 'block';
        // }
        var displayStyle = style ? style : 'block';
        element.style.display = displayStyle;
    }

    /************************************************************
	 * HELPER function
	 *
	 * $() that creates a new DOM element <tag options...>
	 *
	 * ajax() connection function
	 *
	 * @param method -
	 *            GET|POST|PUT|DELETE
	 * @param url -
	 *            API end point
	 * @param callback -
	 *            This the successful callback
	 * @param errorHandler -
	 *            This is the failed callback
	 ***************************************************************/
    function $(tag, options) {
        if (!options) {
            return document.getElementById(tag);
        }

        var element = document.createElement(tag);
        for (var option in options) {
            if (options.hasOwnProperty(option)) {
                element[option] = options[option];
            }
        }

        return element;
    } // End of $()

    function ajax(method, url, data, callback, errorHandler) {
        var xhr = new XMLHttpRequest();

        xhr.open(method, url, true);

        xhr.onload = function() {
            switch (xhr.status) {
                case 200:
                    callback(xhr.responseText);
                    break;
                case 403:
                    onSessionInvalid();
                    break;
                case 401:
                    errorHandler();
                    break;
                // default:
            }
        };

        xhr.onerror = function() {
            console.error("The request couldn't be completed!");
            errorHandler();
        }

        if (data === null) {
            xhr.send();
        } else {
            xhr.setRequestHeader("Content-Type", "application/json;charset=utf-8");
            xhr.send(data);
        }
    } // End of ajax()

    /**************
	* ITEMS/EVENTS
	**************/
    function loadNearbyItems() {
        console.log('loadNearbyItems');
        activeBtn('nearby-btn');

        // The request parameters
        var url = './search';
        var params = 'user_id=' + user_id + '&lat=' + lat + '&lon=' + lng;
        var req = JSON.stringify({});

        // display loading message
        showLoadingMessage('Loading neaby events...');

        // make ajax call
        ajax('GET', url + '?' + params, req,
            // successful callback
            function(res) {
                var items = JSON.parse(res);
                if (!items || items.length === 0) {
                    showWarningMessage('No nearby event!')
                } else {
                    listItems(items);
                }
            },
            // failed callback with error
            function() {
                showErrorMessage('Cannot load nearby events!');
            }
        ); //
    } // End of loadNearbyItems()

    function loadFavoriteItems() {
        activeBtn('fav-btn');

        // The request parameters
        var url = './history';
        var params = 'user_id=' + user_id;
        var req = JSON.stringify({});

        // Display loading message
        showLoadingMessage('Loading favorite items...');

        // make ajax call
        ajax('GET', url + '?' + params, req,
            // callback
            function(res) {
                var items = JSON.parse(res);
                if (!items || items.length === 0) {
                    showWarningMessage('No favorite item!')
                } else {
                    listItems(items);
                }
            },
            // errorHandler
            function() {
                showErrorMessage('Cannot load favorite items!');
            }
        ); //
    } // End of loadFavoriteItems()

    function loadRecommendedItems() {
        activeBtn('recommend-btn');

        // The request parameters
        var url = './recommendation';
        var params = 'user_id=' + user_id + '&lat=' + lat + '&lon=' + lng;
        var req = JSON.stringify({});

        // display loading message
        showLoadingMessage('Loading recommended items...');

        // make ajax call
        ajax('GET', url + '?' + params, req,
            // callback
            function(res) {
                var items = JSON.parse(res);
                if (!items || items.length === 0) {
                    showWarningMessage('No recommended items. Make sure you have favorites.');
                } else {
                    listItems(items);
                }
            },
            // errorHandler
            function() {
                showErrorMessage('Cannot load recommended items!')
            }
        ); //
    } // End of loadRecommendedItems()

    function changeFavoriteItem(item_id) {
        // check whether this item has been visited or not
        var li = $('item-' + item_id);
        var favIcon = $('fav-icon-' + item_id);
        var favorite = li.dataset.favorite !== 'true';

        // The request parameters
        var url = './history';
        var req = JSON.stringify({
            user_id : user_id,
            favorite : [item_id]
        });
        // var method;
        // if (favorite) {
        //     method = 'POST';
        // } else {
        //     method = 'DELETE';
        // }
        var method = favorite ? 'POST' : 'DELETE';

        ajax(method, url, req,
            // callback
            function(res) {
                var result = JSON.parse(res);
                if (result.status === 'OK') {
                    li.dataset.favorite = favorite;
                    // if (favorite) {
                    //     favIcon.className = 'fa fa-heart';
                    // } else {
                    //     favIcon.className = 'fa fa-heart-o';
                    // }
                    favIcon.className = favorite ? 'fa fa-heart' : 'fa fa-heart-o';
                }
            } //
        ); //
    } // End of changeFavoriteItem()

    function listItems(items) {
        // Clear current results
        var itemList = $('item-list');
        itemList.innerHTML = '';

        for (var i = 0; i < items.length; i++) {
            addItem(itemList, items[i]);
        }
    }

    function addItem(itemList, item) {
        var item_id = item.item_id;

        // create the <li> tag and specify the id and class attributes
        var li = $('li', {
            id : 'item-' + item_id,
            className : 'item'
        });

        // set the data attributes
        li.dataset.item_id = item_id;
        li.dataset.favorite = item.favorite;

        // add item image
//        li.appendChild($('img', {
//            src : item.image_url
//        }));
         if (item.image_url) {
             li.appendChild(
                 $('img', {
                     src : item.image_url
                 })
             );
         } else {
             li.appendChild(
                 $('img', {
                     src : 'https://assets-cdn.github.com/images/modules/logos_page/GitHub-Mark.png'
                 })
             );
         }

        // section
        var section = $('div', {});

        // title
        var title = $('a', {
            href : item.url,
            target : '_blank',
            className : 'item-name'
        });
        title.innerHTML = item.name;
        section.appendChild(title);

        // category
        var category = $('p', {
            className : 'item-category'
        });
        category.innerHTML = 'Category: ' + item.categories.join(', ');
        section.appendChild(category);

        // stars: used for rating 
        // here we might have a problem showing 3.5 as 3.
        var stars = $('div', {
            className : 'stars'
        });
        for (var i = 1; i < item.rating; i++) { // start from 1 as not possible for 0 ratings
            var star = $('i', {
                className : 'fa fa-star'
            });
            stars.appendChild(star);
        }
        if (('' + item.rating).match(/\.5$/)) {
            stars.appendChild($('i', {
                className : 'fa fa-star-half-o'
            }));
        }
        section.appendChild(stars);
        li.appendChild(section);

        // address
        var address = $('p', {
            className : 'item-address'
        });
        address.innerHTML = item.address.replace(/,/g, '<br/>'); //.replace(/\"/g, '');
        li.appendChild(address);

        // favorite link
        var favLink = $('p', {
            className : 'fav-link'
        });
        favLink.onclick = function() {
            changeFavoriteItem(item_id);
        };
        favLink.appendChild($('i', {
            id : 'fav-icon-' + item_id,
            className : item.favorite ? 'fa fa-heart' : 'fa fa-heart-o'
        }));
        li.appendChild(favLink);
        itemList.appendChild(li);
    }

    init();

})();
// END

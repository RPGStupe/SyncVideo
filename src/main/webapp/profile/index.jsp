<%@ page import="org.dhbw.mosbach.ai.syncvideo.util.LoginUtil" %>
<html language="de" class="mdc-typography">
<head>
    <%@include file="../res/template/head.jsp" %>
</head>
<body class="mdc-theme--background mdc-typography adjusted-body">
<%@include file="../res/template/header.jsp" %>
<div class="content mdc-toolbar-fixed-adjust">
    <main class="main">
        <%
            if (!LoginUtil.checkCookie(request, "sessionId").equals("") && LoginUtil.checkCookie(request, "loggedIn").equals("true")) {%>
        <div class="mdc-card mdc-card--theme-dark user-card"
             id="banner-div">
            <button class="mdc-fab material-icons" id="banner-button" aria-label="Favorite" onclick="bannerDialog.show()">
                <span class="mdc-fab__icon">
                    edit
                </span>
            </button>
            <section class="mdc-card__primary" style="width:auto;">
                <img class="user-card__avatar" id="avatar-on-card" src="" onclick="avatarDialog.show()">
                <h1 class="mdc-card__title mdc-card__title--large" id="user-name">&nbsp;</h1>
                <h2 class="mdc-card__subtitle"></h2>
            </section>
        </div>
        <h1>History</h1>
        <span id="watchlist-span"></span>
        <%} else {%>
        <h4>Please log in</h4>
        <%}%>
    </main>
    <div class="mdc-snackbar"
         aria-live="assertive"
         aria-atomic="true"
         aria-hidden="true">
        <div class="mdc-snackbar__text"></div>
        <div class="mdc-snackbar__action-wrapper">
            <button type="button" class="mdc-button mdc-snackbar__action-button"></button>
        </div>
    </div>
</div>
<aside id="banner-mdc-dialog"
       class="mdc-dialog"
       role="alertdialog"
       aria-labelledby="my-mdc-dialog-label"
       aria-describedby="my-mdc-dialog-description">
    <div class="mdc-dialog__surface">
        <header class="mdc-dialog__header">
            <h2 id="my-mdc-dialog-label" class="mdc-dialog__header__title">
                Upload Banner
            </h2>
        </header>
        <section id="my-mdc-dialog-description" class="mdc-dialog__body">
            <div class="mdc-text-field">
                <label class="mdc-floating-label" for="banner-text-field">Banner URL</label>
                <input type="text" id="banner-text-field" class="mdc-text-field__input">
                <div class="mdc-line-ripple"></div>
            </div>
        </section>
        <button type="button" class="mdc-button" onclick="uploadBanner()">Upload</button>
    </div>
    <div class="mdc-dialog__backdrop"></div>
</aside>

<aside id="avatar-mdc-dialog"
       class="mdc-dialog"
       role="alertdialog"
       aria-labelledby="my-mdc-dialog-label"
       aria-describedby="my-mdc-dialog-description">
    <div class="mdc-dialog__surface">
        <header class="mdc-dialog__header">
            <h2 id="avatar-mdc-dialog-label" class="mdc-dialog__header__title">
                Upload Banner
            </h2>
        </header>
        <section id="avatar-mdc-dialog-description" class="mdc-dialog__body">
            <div class="mdc-text-field">
                <label class="mdc-floating-label" for="banner-text-field">Avatar URL</label>
                <input type="text" id="avatar-text-field" class="mdc-text-field__input">
                <div class="mdc-line-ripple"></div>
            </div>
        </section>
        <button type="button" class="mdc-button" onclick="uploadAvatar()">Upload</button>
    </div>
    <div class="mdc-dialog__backdrop"></div>
</aside>
<script src="https://unpkg.com/material-components-web@0.26.0/dist/material-components-web.min.js"></script>
<script>
</script>
<script>


    jQuery.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        'type': 'GET',
        'url': '../rest/user/get',
        'dataType': 'json',
        success: function (result) {
            document.getElementById("banner-div").setAttribute("style", "background-image: url(" + result.banner + ");");
            document.getElementById("avatar-on-card").setAttribute("src", result.avatar);
            document.getElementById("user-name").textContent = result.username + "\r\n";
        }
    });

    jQuery.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        'type': 'GET',
        'url': '../rest/watchlist/get',
        'dataType': 'json',
        success: function (result) {
            var listtext = "";
            console.log(result);
            for (var i = result.length - 1; i >= 0; i--) {
                listtext += result[i].url + "<br>";
            }

            document.getElementById("watchlist-span").innerHTML = listtext;
        }
    });

    let bannerDialog = new mdc.dialog.MDCDialog(document.querySelector('#banner-mdc-dialog'));
    let avatarDialog = new mdc.dialog.MDCDialog(document.querySelector('#avatar-mdc-dialog'));
    var timeOut;
    mdc.textField.MDCTextField.attachTo(document.querySelector('.mdc-text-field'));
    var menuEl = document.querySelector('#profile-menu');
    if (menuEl != null) {
        var menuTop = new mdc.menu.MDCSimpleMenu(menuEl);
        menuEl.addEventListener('MDCSimpleMenu:selected', function (evt) {
            menuTop.open = false;
            var detail = evt.detail;
            switch (detail.index) {
                case 0:
                    followLink("/profile/");
                    break;
                case 1:
                    followLink("/watchlist/");
                    break;
                case 2:
                    followLink("/settings/");
                    break;
                case 3:
                    signout();
            }
        });
    }

    function uploadAvatar() {
        jQuery.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'url': document.getElementById('avatar-text-field').value
            },
            'type': 'POST',
            'url': '../rest/user/picture/avatar',
            'dataType': 'html',
            success: function () {
                location.reload();
            }
        });
    }

    function uploadBanner() {
        jQuery.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'url': document.getElementById('banner-text-field').value
            },
            'type': 'POST',
            'url': '../rest/user/picture/banner',
            'dataType': 'html',
            success: function () {
                location.reload();
            }
        });
    }
</script>
</body>
</html>
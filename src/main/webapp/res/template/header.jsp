<%@ page import="de.dieser1memesprech.proxsync.util.LoginUtil" %>
<header id="page-header"
        class="mdc-toolbar mdc-toolbar--fixed">
    <div class="mdc-toolbar__row">
        <section class="mdc-toolbar__section mdc-toolbar__section--align-start">
            <button class="mdc-button mdc-button--raised mdc-theme--secondary-bg"
                    onclick="followLink('${pageContext.request.contextPath}');"
                    id="leave-button"
                    style="align-self: center;margin-left:16px;margin-right: 16px;">New Room
            </button>
        </section>
        <section class="mdc-toolbar__section mdc-toolbar__section--align-middle">
            <span class="mdc-toolbar__title"><a href="#" style="color:inherit;text-decoration:none;" onclick="followLink('${pageContext.request.contextPath}');">SyncVideo</a></span>
        </section>
        <section class="mdc-toolbar__section mdc-toolbar__section--align-end">
            <section class="mdc-toolbar__section mdc-toolbar__section--align-start">
                <div id="register-row"
                     style="align-self:center;float:right;margin-right:16px;margin-left:auto;">
                    <% if (!LoginUtil.checkCookie(request, "loggedIn").equals("true")) {
                        %>
                    <button class="mdc-button mdc-button--raised mdc-theme--secondary-bg mdc-button--align-middle"
                            onclick="loginDialog.show()"
                            id="register-button"
                            style="
                                    align-self: center;
                                    margin-left:10px;">Sign in
                    </button>
                    <% } else {%>
                </div>
                <div id="signout-row" style="align-self: center; margin-right: 16px; margin-left: auto;">
                    <div style="float:right;" onmouseover="clearTimeout(timeOut); menuTop.open = true;"
                         onmouseout="timeOut = setTimeout(function() {menuTop.open = false;},200);"
                         id="profile-mouseaction">
                        <a href="#">
                            <img src="https://firebasestorage.googleapis.com/v0/b/proxsync.appspot.com/o/panda.svg?alt=media&token=6f4d5bf1-af69-4211-994d-66655456d91a"
                                 id="avatar-toolbar" class="user-avatar-toolbar" onclick="followLink('/profile/');">
                        </a>
                        <div class="mdc-simple-menu mdc-simple-menu--open-from-top-right" id="profile-menu"
                             tabindex="-1" style="top:64px;right:-14px;">
                            <ul class="mdc-simple-menu__items mdc-list" role="menu" id="profile-list"
                                aria-hidden="true">
                                <li class="mdc-list-item profile-list" role="menuitem" tabindex="0"
                                    onclick="followLink('/profile/');">
                                    <span style="align-self:center;">Profile</span>
                                </li>
                                <li class="mdc-list-item profile-list" role="menuitem" tabindex="0"
                                    onclick="followLink('/settings/');">
                                    <span style="align-self:center;">Settings</span>
                                </li>
                                <li class="mdc-list-divider" role="separator"></li>
                                <li class="mdc-list-item profile-list" role="menuitem" tabindex="0"
                                    onclick="logout();">
                                    <span style="align-self:center;">Sign Out</span>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <% }%>
            </section>
        </section>
    </div>
</header>
<aside id="login-mdc-dialog"
       class="mdc-dialog"
       role="alertdialog"
       aria-labelledby="my-mdc-dialog-label"
       aria-describedby="my-mdc-dialog-description">
    <div class="mdc-dialog__surface">
        <header class="mdc-dialog__header">
            <h2 id="my-mdc-dialog-label" class="mdc-dialog__header__title">
                Login
            </h2>
        </header>
        <section id="my-mdc-dialog-description" class="mdc-dialog__body">
            <div class="mdc-text-field">
                <label class="mdc-floating-label" for="username-text-field">Username</label>
                <input type="text" id="username-text-field" class="mdc-text-field__input">
                <div class="mdc-line-ripple"></div>
            </div>
            <div class="mdc-text-field">
                <label class="mdc-floating-label" for="password-text-field">Password</label>
                <input type="password" id="password-text-field" class="mdc-text-field__input">
                <div class="mdc-line-ripple"></div>
            </div>
        </section>
        <button type="button" class="mdc-button" onclick="login()">Login</button>
    </div>
    <div class="mdc-dialog__backdrop"></div>
</aside>

<script src="https://unpkg.com/material-components-web@0.26.0/dist/material-components-web.min.js"></script>
<script>
    async function logout() {
        await jQuery.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            'type': 'POST',
            'url': './rest/user/logout',
            'dataType': 'html',
            success: function () {
                location.reload();
            }
        });
    }

    function login() {
        console.log(document.getElementById('password-text-field').value);
        jQuery.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            'type': 'GET',
            'url': './rest/user/login/' + document.getElementById('username-text-field').value + "/" + document.getElementById('password-text-field').value,
            'dataType': 'html',
            success: function () {
                location.reload();
            }
        });
    }

    let loginDialog = new mdc.dialog.MDCDialog(document.querySelector('#login-mdc-dialog'));

    function followLink(loc) {
        window.location = loc;
    }
</script>
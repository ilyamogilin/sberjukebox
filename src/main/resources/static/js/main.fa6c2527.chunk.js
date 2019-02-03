(window.webpackJsonp = window.webpackJsonp || []).push([[0], {
    185: function (e, t, n) {
        e.exports = n(345)
    }, 345: function (e, t, n) {
        "use strict";
        n.r(t);
        var a = n(39), c = n(0), r = n.n(c), i = n(32), o = n.n(i), l = n(40), u = n(33), p = n(34), s = n(37),
            d = n(35), m = n(38), f = n(36), b = n(352), y = n(353), O = n(354), h = n(49), E = n(159),
            v = (n(193), n(30)), j = n(160), x = n.n(j), k = n(161), w = n.n(k), g = "SYNC_TRACK", S = "WAIT_NEXT";

        function N(e) {
            J({type: S}), I(e)
        }

        var C = "SOCKET_START", T = "SOCKET_MESSAGE", R = "SET_CONNECTION", _ = null,
            A = new x.a("http://10.8.30.31:8100/ws");

        function I(e) {
            _.send("/app/test/".concat(e))
        }

        var L = "USER_LOGIN_START", U = "USER_LOGIN_OK", K = "USER_LOGIN_ERR";
        var G = [E.a], P = Object(h.c)({
            user: function (e, t) {
                switch (t.type) {
                    case L:
                        return {fetching: !0};
                    case U:
                        return {fetching: !1, admin: !0};
                    case K:
                        return {fetching: !1, admin: !1};
                    default:
                        return Object(v.a)({}, e)
                }
            }, track: function (e, t) {
                switch (t.type) {
                    case g:
                        return function (e, t) {
                            return e.play ? Object(v.a)({}, e) : Object(v.a)({play: !0}, t.data)
                        }(e, t);
                    case S:
                        return {play: !1};
                    default:
                        return Object(v.a)({}, e)
                }
            }, connection: function (e, t) {
                return t.type === R ? Object(v.a)({}, t.data) : Object(v.a)({}, e)
            }, musicList: function (e, t) {
                switch (t.type) {
                    case C:
                        return Object(v.a)({}, e, t.data);
                    case T:
                        return Object(v.a)({}, t.data);
                    case"SOCKET_ERROR":
                        return Object(v.a)({}, e, t.data);
                    default:
                        return Object(v.a)({}, e)
                }
            }
        }), H = Object(h.d)(P, {
            user: {admin: !1},
            connection: {connected: !1},
            track: {play: !1}
        }, h.a.apply(void 0, G)), J = H.dispatch, W = H, z = n(357), D = n(356);

        function F() {
            var e = Object(a.a)(["\n    display: flex;\n    flex-direction: column;\n    max-width: 500px;\n    width: 96%;\n    margin: 8px;\n    padding: 8px;\n"]);
            return F = function () {
                return e
            }, e
        }

        var B = l.b.div(F()), M = function (e) {
            function t() {
                return Object(u.a)(this, t), Object(s.a)(this, Object(d.a)(t).apply(this, arguments))
            }

            return Object(m.a)(t, e), Object(p.a)(t, [{
                key: "render", value: function () {
                    var e = this.props.playlist;
                    return e && e.list ? r.a.createElement(B, null, r.a.createElement(D.a, {
                        divided: !0,
                        relaxed: !0
                    }, e.list.map(function (e, t) {
                        return r.a.createElement(D.a.Item, {key: t}, r.a.createElement(D.a.Icon, {
                            name: "music",
                            size: "large",
                            verticalAlign: "middle"
                        }), r.a.createElement(D.a.Content, null, r.a.createElement(D.a.Header, null, e.trackName), r.a.createElement(D.a.Description, null, e.artistName)))
                    }))) : r.a.createElement(B, null, r.a.createElement(z.a, {icon: !0}, "Empty playlist..."))
                }
            }]), t
        }(c.Component), X = Object(f.b)(function (e) {
            return {playlist: e.musicList}
        })(M), Y = n(351), q = n(173), Q = n.n(q);

        function V() {
            var e = Object(a.a)(["\n    margin: 0 25px 0 25px;\n"]);
            return V = function () {
                return e
            }, e
        }

        function Z() {
            var e = Object(a.a)(["\n    position: relative;\n    display: flex;\n    justify-content: center;\n    height: 100px;\n"]);
            return Z = function () {
                return e
            }, e
        }

        function $() {
            var e = Object(a.a)(["\n    display: flex;\n    flex-direction: column;\n    max-width: 500px;\n    width: 96%;\n    margin: 8px;\n    padding: 16px;\n    box-shadow: rgba(0, 0, 0, 0.2) 0px 0px 3px 0px;\n"]);
            return $ = function () {
                return e
            }, e
        }

        var ee = l.b.div($()), te = l.b.div(Z()), ne = l.b.div(V()), ae = function () {
            return r.a.createElement(te, null, r.a.createElement(Y.a, {active: !0}, "loading"))
        }, ce = function (e) {
            function t() {
                var e, n;
                Object(u.a)(this, t);
                for (var a = arguments.length, c = new Array(a), r = 0; r < a; r++) c[r] = arguments[r];
                return (n = Object(s.a)(this, (e = Object(d.a)(t)).call.apply(e, [this].concat(c)))).onEnd = function () {
                    N(n.props.id)
                }, n
            }

            return Object(m.a)(t, e), Object(p.a)(t, [{
                key: "shouldComponentUpdate", value: function (e) {
                    return !e.play
                }
            }, {
                key: "componentDidUpdate", value: function (e) {
                    !this.props.play && e.play && this.player.audio.play()
                }
            }, {
                key: "render", value: function () {
                    var e = this, t = this.props.track;
                    if (!t.trackName) return r.a.createElement(ae, null);
                    var n = t.trackUrl, a = t.trackName, c = t.artistName;
                    return r.a.createElement(ee, null, r.a.createElement(ne, null, r.a.createElement(z.a, {size: "medium"}, a), r.a.createElement(z.a.Subheader, null, c)), r.a.createElement(Q.a, {
                        controls: !0,
                        ref: function (t) {
                            return e.player = t
                        },
                        onEnded: this.onEnd,
                        src: n,
                        autoPlay: !0
                    }))
                }
            }]), t
        }(c.Component), re = Object(f.b)(function (e) {
            return {track: e.track}
        })(ce), ie = function (e) {
            function t(e) {
                var n;
                Object(u.a)(this, t), (n = Object(s.a)(this, Object(d.a)(t).call(this, e))).onEnd = function () {
                    N(n.props.match.params.id)
                };
                var a, c = n.props.match.params.id;
                return a = c, (_ = w.a.over(A)).connect({}, function (e) {
                    J({type: C}), J({
                        type: R,
                        data: {connected: !!_.ws.readyState}
                    }), _.subscribe("/topic/test/".concat(a), function (e) {
                        var t = JSON.parse(e.body);
                        t.nowPlaying && J({type: g, data: t.nowPlaying}), t.trackList && J({
                            type: T,
                            data: {list: t.trackList}
                        }), J({type: R, data: {connected: !!_.ws.readyState}})
                    })
                }), n
            }

            return Object(m.a)(t, e), Object(p.a)(t, [{
                key: "componentWillUnmount", value: function () {
                    console.log("disconnect"), _.disconnect()
                }
            }, {
                key: "componentWillReceiveProps", value: function (e) {
                    var t = this.props.connection, n = e.connection;
                    if (!t.connected && n.connected) {
                        var a = this.props.match.params.id;
                        setTimeout(I, 0, a)
                    }
                }
            }, {
                key: "render", value: function () {
                    var e = this.props.match.params.id;
                    return r.a.createElement(r.a.Fragment, null, r.a.createElement(re, {id: e}), r.a.createElement(X, null))
                }
            }]), t
        }(c.Component), oe = Object(f.b)(function (e) {
            return {connection: e.connection}
        })(ie), le = (n(341), function (e) {
            function t() {
                return Object(u.a)(this, t), Object(s.a)(this, Object(d.a)(t).apply(this, arguments))
            }

            return Object(m.a)(t, e), Object(p.a)(t, [{
                key: "render", value: function () {
                    return r.a.createElement(O.a, null, r.a.createElement(f.a, {store: W}, r.a.createElement(b.a, null, r.a.createElement(y.a, {
                        exact: !0,
                        path: "/:id",
                        component: oe
                    }), r.a.createElement(y.a, {
                        exact: !0,
                        path: "/admin",
                        component: oe
                    }), r.a.createElement(y.a, {exact: !0, path: "/admin/:admin", component: oe}))))
                }
            }]), t
        }(c.Component));

        function ue() {
            var e = Object(a.a)(["\n  body {\n    font-family: Lato,'Helvetica Neue',Arial,Helvetica,sans-serif;\n  };\n\n  .flex{\n      box-shadow: none!important;\n  }\n\n  #root{\n      display: flex;\n      flex-direction: column;\n      align-items: center;\n  }\n"]);
            return ue = function () {
                return e
            }, e
        }

        var pe = Object(l.a)(ue());
        o.a.render(r.a.createElement(r.a.Fragment, null, r.a.createElement(le, null), r.a.createElement(pe, null)), document.getElementById("root"))
    }
}, [[185, 2, 1]]]);
//# sourceMappingURL=main.fa6c2527.chunk.js.map
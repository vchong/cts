// Copyright 2012 the Octane Benchmark project authors. All rights reserved.
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are
// met:
//
//     * Redistributions of source code must retain the above copyright
//       notice, this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above
//       copyright notice, this list of conditions and the following
//       disclaimer in the documentation and/or other materials provided
//       with the distribution.
//     * Neither the name of Google Inc. nor the names of its
//       contributors may be used to endorse or promote products derived
//       from this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
// A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
// OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
// LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
// THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.


// The code in BASE_JS below:
// Copyright 2012 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


// The code in JQUERY_JS below:
// Copyright (c) 2012 John Resig, http://jquery.com/
//
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


var CodeLoad = new BenchmarkSuite('CodeLoad', 450000, [
  new Benchmark('CodeLoadClosure',
                runCodeLoadClosure,
                setupCodeLoad,
                tearDownCodeLoad,
                16),
  new Benchmark('CodeLoadJQuery',
                runCodeLoadJQuery,
                setupCodeLoad,
                tearDownCodeLoad,
                16)
]);

var salt;
var indirectEval;

function setupCodeLoad() {
  salt = 0;
  indirectEval = eval;
}

function tearDownCodeLoad() {
  salt = null;
  indirectEval = null;
}

function runCodeLoadClosure() {
  runClosure();
  salt++;
}

function runCodeLoadJQuery() {
  runJQuery();
  salt++;
}

/*
 * BASE_JS is a compiled and formatted version of:
 * http://code.google.com/p/closure-library/source/browse/trunk/closure/goog/base.js
 * JQUERY_JS is a formatted copy of:
 * http://code.jquery.com/jquery.min.js
 * The following Python script generates both variables:

#!/usr/bin/env python

import urllib
import urllib2

def escape_and_format(data, varname):
  data = data.replace("\n", " ").replace("; ", ";")
  data = data.replace("\\", "\\\\").replace("\"", "\\\"")
  data = "var " + varname + " = \"" + data + "\""
  while len(data) > 0:
    cutoff = min(79, len(data))
    while data[cutoff-1] == '\\':
      cutoff -= 1
    line = data[0:cutoff]
    data = data[cutoff:]
    if len(data) > 0:
      line += '\\'
    print line

url = "http://closure-compiler.appspot.com/compile"
request_params = {"output_format": "text",
                  "compilation_level": "SIMPLE_OPTIMIZATIONS",
                  "use_closure_library": "true",
                  "js_code": "",
                  "output_info": "compiled_code"}
result = urllib2.urlopen(url, urllib.urlencode(request_params))
escape_and_format(result.read(), "BASE_JS")

print "\n\n"

url = "http://code.jquery.com/jquery.min.js"
result = urllib2.urlopen(url)
escape_and_format(result.read(), "JQUERY_JS")

*/
var BASE_JS = "var COMPILED=!0,goog=goog||{};goog.global=this;goog.DEBUG=!0;goo\
g.LOCALE=\"en\";goog.provide=function(a){if(!COMPILED){if(goog.isProvided_(a))t\
hrow Error('Namespace \"'+a+'\" already declared.');delete goog.implicitNamespa\
ces_[a];for(var b=a;(b=b.substring(0,b.lastIndexOf(\".\")))&&!goog.getObjectByN\
ame(b);)goog.implicitNamespaces_[b]=!0}goog.exportPath_(a)};goog.setTestOnly=fu\
nction(a){if(COMPILED&&!goog.DEBUG)throw a=a||\"\",Error(\"Importing test-only \
code into non-debug environment\"+a?\": \"+a:\".\");};COMPILED||(goog.isProvide\
d_=function(a){return!goog.implicitNamespaces_[a]&&!!goog.getObjectByName(a)},g\
oog.implicitNamespaces_={});goog.exportPath_=function(a,b,c){a=a.split(\".\");c\
=c||goog.global;!(a[0]in c)&&c.execScript&&c.execScript(\"var \"+a[0]);for(var \
d;a.length&&(d=a.shift());)!a.length&&goog.isDef(b)?c[d]=b:c=c[d]?c[d]:c[d]={}}\
;goog.getObjectByName=function(a,b){for(var c=a.split(\".\"),d=b||goog.global,e\
;e=c.shift();)if(goog.isDefAndNotNull(d[e]))d=d[e];else return null;return d};g\
oog.globalize=function(a,b){var c=b||goog.global,d;for(d in a)c[d]=a[d]};goog.a\
ddDependency=function(a,b,c){if(!COMPILED){for(var d,a=a.replace(/\\\\/g,\"/\")\
,e=goog.dependencies_,g=0;d=b[g];g++){e.nameToPath[d]=a;a in e.pathToNames||(e.\
pathToNames[a]={});e.pathToNames[a][d]=true}for(d=0;b=c[d];d++){a in e.requires\
||(e.requires[a]={});e.requires[a][b]=true}}};goog.ENABLE_DEBUG_LOADER=!0;goog.\
require=function(a){if(!COMPILED&&!goog.isProvided_(a)){if(goog.ENABLE_DEBUG_LO\
ADER){var b=goog.getPathFromDeps_(a);if(b){goog.included_[b]=true;goog.writeScr\
ipts_();return}}a=\"goog.require could not find: \"+a;goog.global.console&&goog\
.global.console.error(a);throw Error(a);}};goog.basePath=\"\";goog.nullFunction\
=function(){};goog.identityFunction=function(a){return a};goog.abstractMethod=f\
unction(){throw Error(\"unimplemented abstract method\");};goog.addSingletonGet\
ter=function(a){a.getInstance=function(){return a.instance_||(a.instance_=new a\
)}};!COMPILED&&goog.ENABLE_DEBUG_LOADER&&(goog.included_={},goog.dependencies_=\
{pathToNames:{},nameToPath:{},requires:{},visited:{},written:{}},goog.inHtmlDoc\
ument_=function(){var a=goog.global.document;return typeof a!=\"undefined\"&&\"\
write\"in a},goog.findBasePath_=function(){if(goog.global.CLOSURE_BASE_PATH)goo\
g.basePath=goog.global.CLOSURE_BASE_PATH;else if(goog.inHtmlDocument_())for(var\
 a=goog.global.document.getElementsByTagName(\"script\"),b=a.length-1;b>=0;--b)\
{var c=a[b].src,d=c.lastIndexOf(\"?\"), d=d==-1?c.length:d;if(c.substr(d-7,7)==\
\"base.js\"){goog.basePath=c.substr(0,d-7);break}}},goog.importScript_=function\
(a){var b=goog.global.CLOSURE_IMPORT_SCRIPT||goog.writeScriptTag_;!goog.depende\
ncies_.written[a]&&b(a)&&(goog.dependencies_.written[a]=true)},goog.writeScript\
Tag_=function(a){if(goog.inHtmlDocument_()){goog.global.document.write('<script\
 type=\"text/javascript\" src=\"'+a+'\"><\\/script>');return true}return false}\
,goog.writeScripts_=function(){function a(e){if(!(e in d.written)){if(!(e in d.\
visited)){d.visited[e]=true;if(e in d.requires)for(var f in d.requires[e])if(!g\
oog.isProvided_(f))if(f in d.nameToPath)a(d.nameToPath[f]);else throw Error(\"U\
ndefined nameToPath for \"+f);}if(!(e in c)){c[e]=true;b.push(e)}}}var b=[],c={\
},d=goog.dependencies_,e;for(e in goog.included_)d.written[e]||a(e);for(e=0;e<b\
.length;e++)if(b[e])goog.importScript_(goog.basePath+b[e]);else throw Error(\"U\
ndefined script input\");},goog.getPathFromDeps_=function(a){return a in goog.d\
ependencies_.nameToPath?goog.dependencies_.nameToPath[a]: null},goog.findBasePa\
th_(),goog.global.CLOSURE_NO_DEPS||goog.importScript_(goog.basePath+\"deps.js\"\
));goog.typeOf=function(a){var b=typeof a;if(b==\"object\")if(a){if(a instanceo\
f Array)return\"array\";if(a instanceof Object)return b;var c=Object.prototype.\
toString.call(a);if(c==\"[object Window]\")return\"object\";if(c==\"[object Arr\
ay]\"||typeof a.length==\"number\"&&typeof a.splice!=\"undefined\"&&typeof a.pr\
opertyIsEnumerable!=\"undefined\"&&!a.propertyIsEnumerable(\"splice\"))return\"\
array\";if(c==\"[object Function]\"||typeof a.call!=\"undefined\"&&typeof a.pro\
pertyIsEnumerable!=\"undefined\"&&!a.propertyIsEnumerable(\"call\"))return\"fun\
ction\"}else return\"null\";else if(b==\"function\"&&typeof a.call==\"undefined\
\")return\"object\";return b};goog.isDef=function(a){return a!==void 0};goog.is\
Null=function(a){return a===null};goog.isDefAndNotNull=function(a){return a!=nu\
ll};goog.isArray=function(a){return goog.typeOf(a)==\"array\"};goog.isArrayLike\
=function(a){var b=goog.typeOf(a);return b==\"array\"||b==\"object\"&&typeof a.\
length==\"number\"};goog.isDateLike=function(a){return goog.isObject(a)&&typeof\
 a.getFullYear==\"function\"};goog.isString=function(a){return typeof a==\"stri\
ng\"};goog.isBoolean=function(a){return typeof a==\"boolean\"};goog.isNumber=fu\
nction(a){return typeof a==\"number\"};goog.isFunction=function(a){return goog.\
typeOf(a)==\"function\"};goog.isObject=function(a){var b=typeof a;return b==\"o\
bject\"&&a!=null||b==\"function\"};goog.getUid=function(a){return a[goog.UID_PR\
OPERTY_]||(a[goog.UID_PROPERTY_]=++goog.uidCounter_)};goog.removeUid=function(a\
){\"removeAttribute\"in a&&a.removeAttribute(goog.UID_PROPERTY_);try{delete a[g\
oog.UID_PROPERTY_]}catch(b){}};goog.UID_PROPERTY_=\"closure_uid_\"+Math.floor(2\
147483648*Math.random()).toString(36);goog.uidCounter_=0;goog.getHashCode=goog.\
getUid;goog.removeHashCode=goog.removeUid;goog.cloneObject=function(a){var b=go\
og.typeOf(a);if(b==\"object\"||b==\"array\"){if(a.clone)return a.clone();var b=\
b==\"array\"?[]:{},c;for(c in a)b[c]=goog.cloneObject(a[c]);return b}return a};\
goog.bindNative_=function(a,b,c){return a.call.apply(a.bind,arguments)};goog.bi\
ndJs_=function(a,b,c){if(!a)throw Error();if(arguments.length>2){var d=Array.pr\
ototype.slice.call(arguments,2);return function(){var c=Array.prototype.slice.c\
all(arguments);Array.prototype.unshift.apply(c,d);return a.apply(b,c)}}return f\
unction(){return a.apply(b,arguments)}};goog.bind=function(a,b,c){goog.bind=Fun\
ction.prototype.bind&&Function.prototype.bind.toString().indexOf(\"native code\
\")!=-1?goog.bindNative_:goog.bindJs_;return goog.bind.apply(null,arguments)};g\
oog.partial=function(a,b){var c=Array.prototype.slice.call(arguments,1);return \
function(){var b=Array.prototype.slice.call(arguments);b.unshift.apply(b,c);ret\
urn a.apply(this,b)}};goog.mixin=function(a,b){for(var c in b)a[c]=b[c]};goog.n\
ow=Date.now||function(){return+new Date};goog.globalEval=function(a){if(goog.gl\
obal.execScript)goog.global.execScript(a,\"JavaScript\");else if(goog.global.ev\
al){if(goog.evalWorksForGlobals_==null){goog.global.eval(\"var _et_ = 1;\");if(\
typeof goog.global._et_!=\"undefined\"){delete goog.global._et_;goog.evalWorksF\
orGlobals_=true}else goog.evalWorksForGlobals_=false}if(goog.evalWorksForGlobal\
s_)goog.global.eval(a);else{var b=goog.global.document,c=b.createElement(\"scri\
pt\");c.type=\"text/javascript\";c.defer=false;c.appendChild(b.createTextNode(a\
));b.body.appendChild(c);b.body.removeChild(c)}}else throw Error(\"goog.globalE\
val not available\");};goog.evalWorksForGlobals_=null;goog.getCssName=function(\
a,b){var c=function(a){return goog.cssNameMapping_[a]||a},d=function(a){for(var\
 a=a.split(\"-\"),b=[],d=0;d<a.length;d++)b.push(c(a[d]));return b.join(\"-\")}\
,d=goog.cssNameMapping_?goog.cssNameMappingStyle_==\"BY_WHOLE\"?c:d:function(a)\
{return a};return b?a+\"-\"+d(b):d(a)};goog.setCssNameMapping=function(a,b){goo\
g.cssNameMapping_=a;goog.cssNameMappingStyle_=b};!COMPILED&&goog.global.CLOSURE\
_CSS_NAME_MAPPING&&(goog.cssNameMapping_=goog.global.CLOSURE_CSS_NAME_MAPPING);\
goog.getMsg=function(a,b){var c=b||{},d;for(d in c)var e=(\"\"+c[d]).replace(/\
\\$/g,\"$$$$\"),a=a.replace(RegExp(\"\\\\{\\\\$\"+d+\"\\\\}\",\"gi\"),e);return\
 a};goog.exportSymbol=function(a,b,c){goog.exportPath_(a,b,c)};goog.exportPrope\
rty=function(a,b,c){a[b]=c};goog.inherits=function(a,b){function c(){}c.prototy\
pe=b.prototype;a.superClass_=b.prototype;a.prototype=new c;a.prototype.construc\
tor=a};goog.base=function(a,b,c){var d=arguments.callee.caller;if(d.superClass_\
)return d.superClass_.constructor.apply(a,Array.prototype.slice.call(arguments,\
1));for(var e=Array.prototype.slice.call(arguments,2),g=false,f=a.constructor;f\
;f=f.superClass_&&f.superClass_.constructor)if(f.prototype[b]===d)g=true;else i\
f(g)return f.prototype[b].apply(a,e);if(a[b]===d)return a.constructor.prototype\
[b].apply(a,e);throw Error(\"goog.base called from a method of one name to a me\
thod of a different name\");};goog.scope=function(a){a.call(goog.global)};"

var JQUERY_JS = "/*! jQuery v1.7.2 jquery.com | jquery.org/license */ (function\
(a,b){function cy(a){return f.isWindow(a)?a:a.nodeType===9?a.defaultView||a.par\
entWindow:!1}function cu(a){if(!cj[a]){var b=c.body,d=f(\"<\"+a+\">\").appendTo\
(b),e=d.css(\"display\");d.remove();if(e===\"none\"||e===\"\"){ck||(ck=c.create\
Element(\"iframe\"),ck.frameBorder=ck.width=ck.height=0),b.appendChild(ck);if(!\
cl||!ck.createElement)cl=(ck.contentWindow||ck.contentDocument).document,cl.wri\
te((f.support.boxModel?\"<!doctype html>\":\"\")+\"<html><body>\"),cl.close();d\
=cl.createElement(a),cl.body.appendChild(d),e=f.css(d,\"display\"),b.removeChil\
d(ck)}cj[a]=e}return cj[a]}function ct(a,b){var c={};f.each(cp.concat.apply([],\
cp.slice(0,b)),function(){c[this]=a});return c}function cs(){cq=b}function cr()\
{setTimeout(cs,0);return cq=f.now()}function ci(){try{return new a.ActiveXObjec\
t(\"Microsoft.XMLHTTP\")}catch(b){}}function ch(){try{return new a.XMLHttpReque\
st}catch(b){}}function cb(a,c){a.dataFilter&&(c=a.dataFilter(c,a.dataType));var\
 d=a.dataTypes,e={},g,h,i=d.length,j,k=d[0],l,m,n,o,p;for(g=1;g<i;g++){if(g===1\
)for(h in a.converters)typeof h==\"string\"&&(e[h.toLowerCase()]=a.converters[h\
]);l=k,k=d[g];if(k===\"*\")k=l;else if(l!==\"*\"&&l!==k){m=l+\" \"+k,n=e[m]||e[\
\"* \"+k];if(!n){p=b;for(o in e){j=o.split(\" \");if(j[0]===l||j[0]===\"*\"){p=\
e[j[1]+\" \"+k];if(p){o=e[o],o===!0?n=p:p===!0&&(n=o);break}}}}!n&&!p&&f.error(\
\"No conversion from \"+m.replace(\" \",\" to \")),n!==!0&&(c=n?n(c):p(o(c)))}}\
return c}function ca(a,c,d){var e=a.contents,f=a.dataTypes,g=a.responseFields,h\
,i,j,k;for(i in g)i in d&&(c[g[i]]=d[i]);while(f[0]===\"*\")f.shift(),h===b&&(h\
=a.mimeType||c.getResponseHeader(\"content-type\"));if(h)for(i in e)if(e[i]&&e[\
i].test(h)){f.unshift(i);break}if(f[0]in d)j=f[0];else{for(i in d){if(!f[0]||a.\
converters[i+\" \"+f[0]]){j=i;break}k||(k=i)}j=j||k}if(j){j!==f[0]&&f.unshift(j\
);return d[j]}}function b_(a,b,c,d){if(f.isArray(b))f.each(b,function(b,e){c||b\
D.test(a)?d(a,e):b_(a+\"[\"+(typeof e==\"object\"?b:\"\")+\"]\",e,c,d)});else i\
f(!c&&f.type(b)===\"object\")for(var e in b)b_(a+\"[\"+e+\"]\",b[e],c,d);else d\
(a,b)}function b$(a,c){var d,e,g=f.ajaxSettings.flatOptions||{};for(d in c)c[d]\
!==b&&((g[d]?a:e||(e={}))[d]=c[d]);e&&f.extend(!0,a,e)}function bZ(a,c,d,e,f,g)\
{f=f||c.dataTypes[0],g=g||{},g[f]=!0;var h=a[f],i=0,j=h?h.length:0,k=a===bS,l;f\
or(;i<j&&(k||!l);i++)l=h[i](c,d,e),typeof l==\"string\"&&(!k||g[l]?l=b:(c.dataT\
ypes.unshift(l),l=bZ(a,c,d,e,l,g)));(k||!l)&&!g[\"*\"]&&(l=bZ(a,c,d,e,\"*\",g))\
;return l}function bY(a){return function(b,c){typeof b!=\"string\"&&(c=b,b=\"*\
\");if(f.isFunction(c)){var d=b.toLowerCase().split(bO),e=0,g=d.length,h,i,j;fo\
r(;e<g;e++)h=d[e],j=/^\\+/.test(h),j&&(h=h.substr(1)||\"*\"),i=a[h]=a[h]||[],i[\
j?\"unshift\":\"push\"](c)}}}function bB(a,b,c){var d=b===\"width\"?a.offsetWid\
th:a.offsetHeight,e=b===\"width\"?1:0,g=4;if(d>0){if(c!==\"border\")for(;e<g;e+\
=2)c||(d-=parseFloat(f.css(a,\"padding\"+bx[e]))||0),c===\"margin\"?d+=parseFlo\
at(f.css(a,c+bx[e]))||0:d-=parseFloat(f.css(a,\"border\"+bx[e]+\"Width\"))||0;r\
eturn d+\"px\"}d=by(a,b);if(d<0||d==null)d=a.style[b];if(bt.test(d))return d;d=\
parseFloat(d)||0;if(c)for(;e<g;e+=2)d+=parseFloat(f.css(a,\"padding\"+bx[e]))||\
0,c!==\"padding\"&&(d+=parseFloat(f.css(a,\"border\"+bx[e]+\"Width\"))||0),c===\
\"margin\"&&(d+=parseFloat(f.css(a,c+bx[e]))||0);return d+\"px\"}function bo(a)\
{var b=c.createElement(\"div\");bh.appendChild(b),b.innerHTML=a.outerHTML;retur\
n b.firstChild}function bn(a){var b=(a.nodeName||\"\").toLowerCase();b===\"inpu\
t\"?bm(a):b!==\"script\"&&typeof a.getElementsByTagName!=\"undefined\"&&f.grep(\
a.getElementsByTagName(\"input\"),bm)}function bm(a){if(a.type===\"checkbox\"||\
a.type===\"radio\")a.defaultChecked=a.checked}function bl(a){return typeof a.ge\
tElementsByTagName!=\"undefined\"?a.getElementsByTagName(\"*\"):typeof a.queryS\
electorAll!=\"undefined\"?a.querySelectorAll(\"*\"):[]}function bk(a,b){var c;b\
.nodeType===1&&(b.clearAttributes&&b.clearAttributes(),b.mergeAttributes&&b.mer\
geAttributes(a),c=b.nodeName.toLowerCase(),c===\"object\"?b.outerHTML=a.outerHT\
ML:c!==\"input\"||a.type!==\"checkbox\"&&a.type!==\"radio\"?c===\"option\"?b.se\
lected=a.defaultSelected:c===\"input\"||c===\"textarea\"?b.defaultValue=a.defau\
ltValue:c===\"script\"&&b.text!==a.text&&(b.text=a.text):(a.checked&&(b.default\
Checked=b.checked=a.checked),b.value!==a.value&&(b.value=a.value)),b.removeAttr\
ibute(f.expando),b.removeAttribute(\"_submit_attached\"),b.removeAttribute(\"_c\
hange_attached\"))}function bj(a,b){if(b.nodeType===1&&!!f.hasData(a)){var c,d,\
e,g=f._data(a),h=f._data(b,g),i=g.events;if(i){delete h.handle,h.events={};for(\
c in i)for(d=0,e=i[c].length;d<e;d++)f.event.add(b,c,i[c][d])}h.data&&(h.data=f\
.extend({},h.data))}}function bi(a,b){return f.nodeName(a,\"table\")?a.getEleme\
ntsByTagName(\"tbody\")[0]||a.appendChild(a.ownerDocument.createElement(\"tbody\
\")):a}function U(a){var b=V.split(\"|\"),c=a.createDocumentFragment();if(c.cre\
ateElement)while(b.length)c.createElement(b.pop());return c}function T(a,b,c){b\
=b||0;if(f.isFunction(b))return f.grep(a,function(a,d){var e=!!b.call(a,d,a);re\
turn e===c});if(b.nodeType)return f.grep(a,function(a,d){return a===b===c});if(\
typeof b==\"string\"){var d=f.grep(a,function(a){return a.nodeType===1});if(O.t\
est(b))return f.filter(b,d,!c);b=f.filter(b,d)}return f.grep(a,function(a,d){re\
turn f.inArray(a,b)>=0===c})}function S(a){return!a||!a.parentNode||a.parentNod\
e.nodeType===11}function K(){return!0}function J(){return!1}function n(a,b,c){v\
ar d=b+\"defer\",e=b+\"queue\",g=b+\"mark\",h=f._data(a,d);h&&(c===\"queue\"||!\
f._data(a,e))&&(c===\"mark\"||!f._data(a,g))&&setTimeout(function(){!f._data(a,\
e)&&!f._data(a,g)&&(f.removeData(a,d,!0),h.fire())},0)}function m(a){for(var b \
in a){if(b===\"data\"&&f.isEmptyObject(a[b]))continue;if(b!==\"toJSON\")return!\
1}return!0}function l(a,c,d){if(d===b&&a.nodeType===1){var e=\"data-\"+c.replac\
e(k,\"-$1\").toLowerCase();d=a.getAttribute(e);if(typeof d==\"string\"){try{d=d\
===\"true\"?!0:d===\"false\"?!1:d===\"null\"?null:f.isNumeric(d)?+d:j.test(d)?f\
.parseJSON(d):d}catch(g){}f.data(a,c,d)}else d=b}return d}function h(a){var b=g\
[a]={},c,d;a=a.split(/\\s+/);for(c=0,d=a.length;c<d;c++)b[a[c]]=!0;return b}var\
 c=a.document,d=a.navigator,e=a.location,f=function(){function J(){if(!e.isRead\
y){try{c.documentElement.doScroll(\"left\")}catch(a){setTimeout(J,1);return}e.r\
eady()}}var e=function(a,b){return new e.fn.init(a,b,h)},f=a.jQuery,g=a.$,h,i=/\
^(?:[^#<]*(<[\\w\\W]+>)[^>]*$|#([\\w\\-]*)$)/,j=/\\S/,k=/^\\s+/,l=/\\s+$/,m=/^<\
(\\w+)\\s*\\/?>(?:<\\/\\1>)?$/,n=/^[\\],:{}\\s]*$/,o=/\\\\(?:[\"\\\\\\/bfnrt]|u\
[0-9a-fA-F]{4})/g,p=/\"[^\"\\\\\\n\\r]*\"|true|false|null|-?\\d+(?:\\.\\d*)?(?:\
[eE][+\\-]?\\d+)?/g,q=/(?:^|:|,)(?:\\s*\\[)+/g,r=/(webkit)[ \\/]([\\w.]+)/,s=/(\
opera)(?:.*version)?[ \\/]([\\w.]+)/,t=/(msie) ([\\w.]+)/,u=/(mozilla)(?:.*? rv\
:([\\w.]+))?/,v=/-([a-z]|[0-9])/ig,w=/^-ms-/,x=function(a,b){return(b+\"\").toU\
pperCase()},y=d.userAgent,z,A,B,C=Object.prototype.toString,D=Object.prototype.\
hasOwnProperty,E=Array.prototype.push,F=Array.prototype.slice,G=String.prototyp\
e.trim,H=Array.prototype.indexOf,I={};e.fn=e.prototype={constructor:e,init:func\
tion(a,d,f){var g,h,j,k;if(!a)return this;if(a.nodeType){this.context=this[0]=a\
,this.length=1;return this}if(a===\"body\"&&!d&&c.body){this.context=c,this[0]=\
c.body,this.selector=a,this.length=1;return this}if(typeof a==\"string\"){a.cha\
rAt(0)!==\"<\"||a.charAt(a.length-1)!==\">\"||a.length<3?g=i.exec(a):g=[null,a,\
null];if(g&&(g[1]||!d)){if(g[1]){d=d instanceof e?d[0]:d,k=d?d.ownerDocument||d\
:c,j=m.exec(a),j?e.isPlainObject(d)?(a=[c.createElement(j[1])],e.fn.attr.call(a\
,d,!0)):a=[k.createElement(j[1])]:(j=e.buildFragment([g[1]],[k]),a=(j.cacheable\
?e.clone(j.fragment):j.fragment).childNodes);return e.merge(this,a)}h=c.getElem\
entById(g[2]);if(h&&h.parentNode){if(h.id!==g[2])return f.find(a);this.length=1\
,this[0]=h}this.context=c,this.selector=a;return this}return!d||d.jquery?(d||f)\
.find(a):this.constructor(d).find(a)}if(e.isFunction(a))return f.ready(a);a.sel\
ector!==b&&(this.selector=a.selector,this.context=a.context);return e.makeArray\
(a,this)},selector:\"\",jquery:\"1.7.2\",length:0,size:function(){return this.l\
ength},toArray:function(){return F.call(this,0)},get:function(a){return a==null\
?this.toArray():a<0?this[this.length+a]:this[a]},pushStack:function(a,b,c){var \
d=this.constructor();e.isArray(a)?E.apply(d,a):e.merge(d,a),d.prevObject=this,d\
.context=this.context,b===\"find\"?d.selector=this.selector+(this.selector?\" \
\":\"\")+c:b&&(d.selector=this.selector+\".\"+b+\"(\"+c+\")\");return d},each:f\
unction(a,b){return e.each(this,a,b)},ready:function(a){e.bindReady(),A.add(a);\
return this},eq:function(a){a=+a;return a===-1?this.slice(a):this.slice(a,a+1)}\
,first:function(){return this.eq(0)},last:function(){return this.eq(-1)},slice:\
function(){return this.pushStack(F.apply(this,arguments),\"slice\",F.call(argum\
ents).join(\",\"))},map:function(a){return this.pushStack(e.map(this,function(b\
,c){return a.call(b,c,b)}))},end:function(){return this.prevObject||this.constr\
uctor(null)},push:E,sort:[].sort,splice:[].splice},e.fn.init.prototype=e.fn,e.e\
xtend=e.fn.extend=function(){var a,c,d,f,g,h,i=arguments[0]||{},j=1,k=arguments\
.length,l=!1;typeof i==\"boolean\"&&(l=i,i=arguments[1]||{},j=2),typeof i!=\"ob\
ject\"&&!e.isFunction(i)&&(i={}),k===j&&(i=this,--j);for(;j<k;j++)if((a=argumen\
ts[j])!=null)for(c in a){d=i[c],f=a[c];if(i===f)continue;l&&f&&(e.isPlainObject\
(f)||(g=e.isArray(f)))?(g?(g=!1,h=d&&e.isArray(d)?d:[]):h=d&&e.isPlainObject(d)\
?d:{},i[c]=e.extend(l,h,f)):f!==b&&(i[c]=f)}return i},e.extend({noConflict:func\
tion(b){a.$===e&&(a.$=g),b&&a.jQuery===e&&(a.jQuery=f);return e},isReady:!1,rea\
dyWait:1,holdReady:function(a){a?e.readyWait++:e.ready(!0)},ready:function(a){i\
f(a===!0&&!--e.readyWait||a!==!0&&!e.isReady){if(!c.body)return setTimeout(e.re\
ady,1);e.isReady=!0;if(a!==!0&&--e.readyWait>0)return;A.fireWith(c,[e]),e.fn.tr\
igger&&e(c).trigger(\"ready\").off(\"ready\")}},bindReady:function(){if(!A){A=e\
.Callbacks(\"once memory\");if(c.readyState===\"complete\")return setTimeout(e.\
ready,1);if(c.addEventListener)c.addEventListener(\"DOMContentLoaded\",B,!1),a.\
addEventListener(\"load\",e.ready,!1);else if(c.attachEvent){c.attachEvent(\"on\
readystatechange\",B),a.attachEvent(\"onload\",e.ready);var b=!1;try{b=a.frameE\
lement==null}catch(d){}c.documentElement.doScroll&&b&&J()}}},isFunction:functio\
n(a){return e.type(a)===\"function\"},isArray:Array.isArray||function(a){return\
 e.type(a)===\"array\"},isWindow:function(a){return a!=null&&a==a.window},isNum\
eric:function(a){return!isNaN(parseFloat(a))&&isFinite(a)},type:function(a){ret\
urn a==null?String(a):I[C.call(a)]||\"object\"},isPlainObject:function(a){if(!a\
||e.type(a)!==\"object\"||a.nodeType||e.isWindow(a))return!1;try{if(a.construct\
or&&!D.call(a,\"constructor\")&&!D.call(a.constructor.prototype,\"isPrototypeOf\
\"))return!1}catch(c){return!1}var d;for(d in a);return d===b||D.call(a,d)},isE\
mptyObject:function(a){for(var b in a)return!1;return!0},error:function(a){thro\
w new Error(a)},parseJSON:function(b){if(typeof b!=\"string\"||!b)return null;b\
=e.trim(b);if(a.JSON&&a.JSON.parse)return a.JSON.parse(b);if(n.test(b.replace(o\
,\"@\").replace(p,\"]\").replace(q,\"\")))return(new Function(\"return \"+b))()\
;e.error(\"Invalid JSON: \"+b)},parseXML:function(c){if(typeof c!=\"string\"||!\
c)return null;var d,f;try{a.DOMParser?(f=new DOMParser,d=f.parseFromString(c,\"\
text/xml\")):(d=new ActiveXObject(\"Microsoft.XMLDOM\"),d.async=\"false\",d.loa\
dXML(c))}catch(g){d=b}(!d||!d.documentElement||d.getElementsByTagName(\"parsere\
rror\").length)&&e.error(\"Invalid XML: \"+c);return d},noop:function(){},globa\
lEval:function(b){b&&j.test(b)&&(a.execScript||function(b){a.eval.call(a,b)})(b\
)},camelCase:function(a){return a.replace(w,\"ms-\").replace(v,x)},nodeName:fun\
ction(a,b){return a.nodeName&&a.nodeName.toUpperCase()===b.toUpperCase()},each:\
function(a,c,d){var f,g=0,h=a.length,i=h===b||e.isFunction(a);if(d){if(i){for(f\
 in a)if(c.apply(a[f],d)===!1)break}else for(;g<h;)if(c.apply(a[g++],d)===!1)br\
eak}else if(i){for(f in a)if(c.call(a[f],f,a[f])===!1)break}else for(;g<h;)if(c\
.call(a[g],g,a[g++])===!1)break;return a},trim:G?function(a){return a==null?\"\
\":G.call(a)}:function(a){return a==null?\"\":(a+\"\").replace(k,\"\").replace(\
l,\"\")},makeArray:function(a,b){var c=b||[];if(a!=null){var d=e.type(a);a.leng\
th==null||d===\"string\"||d===\"function\"||d===\"regexp\"||e.isWindow(a)?E.cal\
l(c,a):e.merge(c,a)}return c},inArray:function(a,b,c){var d;if(b){if(H)return H\
.call(b,a,c);d=b.length,c=c?c<0?Math.max(0,d+c):c:0;for(;c<d;c++)if(c in b&&b[c\
]===a)return c}return-1},merge:function(a,c){var d=a.length,e=0;if(typeof c.len\
gth==\"number\")for(var f=c.length;e<f;e++)a[d++]=c[e];else while(c[e]!==b)a[d+\
+]=c[e++];a.length=d;return a},grep:function(a,b,c){var d=[],e;c=!!c;for(var f=\
0,g=a.length;f<g;f++)e=!!b(a[f],f),c!==e&&d.push(a[f]);return d},map:function(a\
,c,d){var f,g,h=[],i=0,j=a.length,k=a instanceof e||j!==b&&typeof j==\"number\"\
&&(j>0&&a[0]&&a[j-1]||j===0||e.isArray(a));if(k)for(;i<j;i++)f=c(a[i],i,d),f!=n\
ull&&(h[h.length]=f);else for(g in a)f=c(a[g],g,d),f!=null&&(h[h.length]=f);ret\
urn h.concat.apply([],h)},guid:1,proxy:function(a,c){if(typeof c==\"string\"){v\
ar d=a[c];c=a,a=d}if(!e.isFunction(a))return b;var f=F.call(arguments,2),g=func\
tion(){return a.apply(c,f.concat(F.call(arguments)))};g.guid=a.guid=a.guid||g.g\
uid||e.guid++;return g},access:function(a,c,d,f,g,h,i){var j,k=d==null,l=0,m=a.\
length;if(d&&typeof d==\"object\"){for(l in d)e.access(a,c,l,d[l],1,h,f);g=1}el\
se if(f!==b){j=i===b&&e.isFunction(f),k&&(j?(j=c,c=function(a,b,c){return j.cal\
l(e(a),c)}):(c.call(a,f),c=null));if(c)for(;l<m;l++)c(a[l],d,j?f.call(a[l],l,c(\
a[l],d)):f,i);g=1}return g?a:k?c.call(a):m?c(a[0],d):h},now:function(){return(n\
ew Date).getTime()},uaMatch:function(a){a=a.toLowerCase();var b=r.exec(a)||s.ex\
ec(a)||t.exec(a)||a.indexOf(\"compatible\")<0&&u.exec(a)||[];return{browser:b[1\
]||\"\",version:b[2]||\"0\"}},sub:function(){function a(b,c){return new a.fn.in\
it(b,c)}e.extend(!0,a,this),a.superclass=this,a.fn=a.prototype=this(),a.fn.cons\
tructor=a,a.sub=this.sub,a.fn.init=function(d,f){f&&f instanceof e&&!(f instanc\
eof a)&&(f=a(f));return e.fn.init.call(this,d,f,b)},a.fn.init.prototype=a.fn;va\
r b=a(c);return a},browser:{}}),e.each(\"Boolean Number String Function Array D\
ate RegExp Object\".split(\" \"),function(a,b){I[\"[object \"+b+\"]\"]=b.toLowe\
rCase()}),z=e.uaMatch(y),z.browser&&(e.browser[z.browser]=!0,e.browser.version=\
z.version),e.browser.webkit&&(e.browser.safari=!0),j.test(\" \")&&(k=/^[\\s\\x\
A0]+/,l=/[\\s\\xA0]+$/),h=e(c),c.addEventListener?B=function(){c.removeEventLis\
tener(\"DOMContentLoaded\",B,!1),e.ready()}:c.attachEvent&&(B=function(){c.read\
yState===\"complete\"&&(c.detachEvent(\"onreadystatechange\",B),e.ready())});re\
turn e}(),g={};f.Callbacks=function(a){a=a?g[a]||h(a):{};var c=[],d=[],e,i,j,k,\
l,m,n=function(b){var d,e,g,h,i;for(d=0,e=b.length;d<e;d++)g=b[d],h=f.type(g),h\
===\"array\"?n(g):h===\"function\"&&(!a.unique||!p.has(g))&&c.push(g)},o=functi\
on(b,f){f=f||[],e=!a.memory||[b,f],i=!0,j=!0,m=k||0,k=0,l=c.length;for(;c&&m<l;\
m++)if(c[m].apply(b,f)===!1&&a.stopOnFalse){e=!0;break}j=!1,c&&(a.once?e===!0?p\
.disable():c=[]:d&&d.length&&(e=d.shift(),p.fireWith(e[0],e[1])))},p={add:funct\
ion(){if(c){var a=c.length;n(arguments),j?l=c.length:e&&e!==!0&&(k=a,o(e[0],e[1\
]))}return this},remove:function(){if(c){var b=arguments,d=0,e=b.length;for(;d<\
e;d++)for(var f=0;f<c.length;f++)if(b[d]===c[f]){j&&f<=l&&(l--,f<=m&&m--),c.spl\
ice(f--,1);if(a.unique)break}}return this},has:function(a){if(c){var b=0,d=c.le\
ngth;for(;b<d;b++)if(a===c[b])return!0}return!1},empty:function(){c=[];return t\
his},disable:function(){c=d=e=b;return this},disabled:function(){return!c},lock\
:function(){d=b,(!e||e===!0)&&p.disable();return this},locked:function(){return\
!d},fireWith:function(b,c){d&&(j?a.once||d.push([b,c]):(!a.once||!e)&&o(b,c));r\
eturn this},fire:function(){p.fireWith(this,arguments);return this},fired:funct\
ion(){return!!i}};return p};var i=[].slice;f.extend({Deferred:function(a){var b\
=f.Callbacks(\"once memory\"),c=f.Callbacks(\"once memory\"),d=f.Callbacks(\"me\
mory\"),e=\"pending\",g={resolve:b,reject:c,notify:d},h={done:b.add,fail:c.add,\
progress:d.add,state:function(){return e},isResolved:b.fired,isRejected:c.fired\
,then:function(a,b,c){i.done(a).fail(b).progress(c);return this},always:functio\
n(){i.done.apply(i,arguments).fail.apply(i,arguments);return this},pipe:functio\
n(a,b,c){return f.Deferred(function(d){f.each({done:[a,\"resolve\"],fail:[b,\"r\
eject\"],progress:[c,\"notify\"]},function(a,b){var c=b[0],e=b[1],g;f.isFunctio\
n(c)?i[a](function(){g=c.apply(this,arguments),g&&f.isFunction(g.promise)?g.pro\
mise().then(d.resolve,d.reject,d.notify):d[e+\"With\"](this===i?d:this,[g])}):i\
[a](d[e])})}).promise()},promise:function(a){if(a==null)a=h;else for(var b in h\
)a[b]=h[b];return a}},i=h.promise({}),j;for(j in g)i[j]=g[j].fire,i[j+\"With\"]\
=g[j].fireWith;i.done(function(){e=\"resolved\"},c.disable,d.lock).fail(functio\
n(){e=\"rejected\"},b.disable,d.lock),a&&a.call(i,i);return i},when:function(a)\
{function m(a){return function(b){e[a]=arguments.length>1?i.call(arguments,0):b\
,j.notifyWith(k,e)}}function l(a){return function(c){b[a]=arguments.length>1?i.\
call(arguments,0):c,--g||j.resolveWith(j,b)}}var b=i.call(arguments,0),c=0,d=b.\
length,e=Array(d),g=d,h=d,j=d<=1&&a&&f.isFunction(a.promise)?a:f.Deferred(),k=j\
.promise();if(d>1){for(;c<d;c++)b[c]&&b[c].promise&&f.isFunction(b[c].promise)?\
b[c].promise().then(l(c),j.reject,m(c)):--g;g||j.resolveWith(j,b)}else j!==a&&j\
.resolveWith(j,d?[a]:[]);return k}}),f.support=function(){var b,d,e,g,h,i,j,k,l\
,m,n,o,p=c.createElement(\"div\"),q=c.documentElement;p.setAttribute(\"classNam\
e\",\"t\"),p.innerHTML=\"   <link/><table></table><a href='/a' style='top:1px;f\
loat:left;opacity:.55;'>a</a><input type='checkbox'/>\",d=p.getElementsByTagNam\
e(\"*\"),e=p.getElementsByTagName(\"a\")[0];if(!d||!d.length||!e)return{};g=c.c\
reateElement(\"select\"),h=g.appendChild(c.createElement(\"option\")),i=p.getEl\
ementsByTagName(\"input\")[0],b={leadingWhitespace:p.firstChild.nodeType===3,tb\
ody:!p.getElementsByTagName(\"tbody\").length,htmlSerialize:!!p.getElementsByTa\
gName(\"link\").length,style:/top/.test(e.getAttribute(\"style\")),hrefNormaliz\
ed:e.getAttribute(\"href\")===\"/a\",opacity:/^0.55/.test(e.style.opacity),cssF\
loat:!!e.style.cssFloat,checkOn:i.value===\"on\",optSelected:h.selected,getSetA\
ttribute:p.className!==\"t\",enctype:!!c.createElement(\"form\").enctype,html5C\
lone:c.createElement(\"nav\").cloneNode(!0).outerHTML!==\"<:nav></:nav>\",submi\
tBubbles:!0,changeBubbles:!0,focusinBubbles:!1,deleteExpando:!0,noCloneEvent:!0\
,inlineBlockNeedsLayout:!1,shrinkWrapBlocks:!1,reliableMarginRight:!0,pixelMarg\
in:!0},f.boxModel=b.boxModel=c.compatMode===\"CSS1Compat\",i.checked=!0,b.noClo\
neChecked=i.cloneNode(!0).checked,g.disabled=!0,b.optDisabled=!h.disabled;try{d\
elete p.test}catch(r){b.deleteExpando=!1}!p.addEventListener&&p.attachEvent&&p.\
fireEvent&&(p.attachEvent(\"onclick\",function(){b.noCloneEvent=!1}),p.cloneNod\
e(!0).fireEvent(\"onclick\")),i=c.createElement(\"input\"),i.value=\"t\",i.setA\
ttribute(\"type\",\"radio\"),b.radioValue=i.value===\"t\",i.setAttribute(\"chec\
ked\",\"checked\"),i.setAttribute(\"name\",\"t\"),p.appendChild(i),j=c.createDo\
cumentFragment(),j.appendChild(p.lastChild),b.checkClone=j.cloneNode(!0).cloneN\
ode(!0).lastChild.checked,b.appendChecked=i.checked,j.removeChild(i),j.appendCh\
ild(p);if(p.attachEvent)for(n in{submit:1,change:1,focusin:1})m=\"on\"+n,o=m in\
 p,o||(p.setAttribute(m,\"return;\"),o=typeof p[m]==\"function\"),b[n+\"Bubbles\
\"]=o;j.removeChild(p),j=g=h=p=i=null,f(function(){var d,e,g,h,i,j,l,m,n,q,r,s,\
t,u=c.getElementsByTagName(\"body\")[0];!u||(m=1,t=\"padding:0;margin:0;border:\
\",r=\"position:absolute;top:0;left:0;width:1px;height:1px;\",s=t+\"0;visibilit\
y:hidden;\",n=\"style='\"+r+t+\"5px solid #000;\",q=\"<div \"+n+\"display:block\
;'><div style='\"+t+\"0;display:block;overflow:hidden;'></div></div>\"+\"<table\
 \"+n+\"' cellpadding='0' cellspacing='0'>\"+\"<tr><td></td></tr></table>\",d=c\
.createElement(\"div\"),d.style.cssText=s+\"width:0;height:0;position:static;to\
p:0;margin-top:\"+m+\"px\",u.insertBefore(d,u.firstChild),p=c.createElement(\"d\
iv\"),d.appendChild(p),p.innerHTML=\"<table><tr><td style='\"+t+\"0;display:non\
e'></td><td>t</td></tr></table>\",k=p.getElementsByTagName(\"td\"),o=k[0].offse\
tHeight===0,k[0].style.display=\"\",k[1].style.display=\"none\",b.reliableHidde\
nOffsets=o&&k[0].offsetHeight===0,a.getComputedStyle&&(p.innerHTML=\"\",l=c.cre\
ateElement(\"div\"),l.style.width=\"0\",l.style.marginRight=\"0\",p.style.width\
=\"2px\",p.appendChild(l),b.reliableMarginRight=(parseInt((a.getComputedStyle(l\
,null)||{marginRight:0}).marginRight,10)||0)===0),typeof p.style.zoom!=\"undefi\
ned\"&&(p.innerHTML=\"\",p.style.width=p.style.padding=\"1px\",p.style.border=0\
,p.style.overflow=\"hidden\",p.style.display=\"inline\",p.style.zoom=1,b.inline\
BlockNeedsLayout=p.offsetWidth===3,p.style.display=\"block\",p.style.overflow=\
\"visible\",p.innerHTML=\"<div style='width:5px;'></div>\",b.shrinkWrapBlocks=p\
.offsetWidth!==3),p.style.cssText=r+s,p.innerHTML=q,e=p.firstChild,g=e.firstChi\
ld,i=e.nextSibling.firstChild.firstChild,j={doesNotAddBorder:g.offsetTop!==5,do\
esAddBorderForTableAndCells:i.offsetTop===5},g.style.position=\"fixed\",g.style\
.top=\"20px\",j.fixedPosition=g.offsetTop===20||g.offsetTop===15,g.style.positi\
on=g.style.top=\"\",e.style.overflow=\"hidden\",e.style.position=\"relative\",j\
.subtractsBorderForOverflowNotVisible=g.offsetTop===-5,j.doesNotIncludeMarginIn\
BodyOffset=u.offsetTop!==m,a.getComputedStyle&&(p.style.marginTop=\"1%\",b.pixe\
lMargin=(a.getComputedStyle(p,null)||{marginTop:0}).marginTop!==\"1%\"),typeof \
d.style.zoom!=\"undefined\"&&(d.style.zoom=1),u.removeChild(d),l=p=d=null,f.ext\
end(b,j))});return b}();var j=/^(?:\\{.*\\}|\\[.*\\])$/,k=/([A-Z])/g;f.extend({\
cache:{},uuid:0,expando:\"jQuery\"+(f.fn.jquery+Math.random()).replace(/\\D/g,\
\"\"),noData:{embed:!0,object:\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\",ap\
plet:!0},hasData:function(a){a=a.nodeType?f.cache[a[f.expando]]:a[f.expando];re\
turn!!a&&!m(a)},data:function(a,c,d,e){if(!!f.acceptData(a)){var g,h,i,j=f.expa\
ndo,k=typeof c==\"string\",l=a.nodeType,m=l?f.cache:a,n=l?a[j]:a[j]&&j,o=c===\"\
events\";if((!n||!m[n]||!o&&!e&&!m[n].data)&&k&&d===b)return;n||(l?a[j]=n=++f.u\
uid:n=j),m[n]||(m[n]={},l||(m[n].toJSON=f.noop));if(typeof c==\"object\"||typeo\
f c==\"function\")e?m[n]=f.extend(m[n],c):m[n].data=f.extend(m[n].data,c);g=h=m\
[n],e||(h.data||(h.data={}),h=h.data),d!==b&&(h[f.camelCase(c)]=d);if(o&&!h[c])\
return g.events;k?(i=h[c],i==null&&(i=h[f.camelCase(c)])):i=h;return i}},remove\
Data:function(a,b,c){if(!!f.acceptData(a)){var d,e,g,h=f.expando,i=a.nodeType,j\
=i?f.cache:a,k=i?a[h]:h;if(!j[k])return;if(b){d=c?j[k]:j[k].data;if(d){f.isArra\
y(b)||(b in d?b=[b]:(b=f.camelCase(b),b in d?b=[b]:b=b.split(\" \")));for(e=0,g\
=b.length;e<g;e++)delete d[b[e]];if(!(c?m:f.isEmptyObject)(d))return}}if(!c){de\
lete j[k].data;if(!m(j[k]))return}f.support.deleteExpando||!j.setInterval?delet\
e j[k]:j[k]=null,i&&(f.support.deleteExpando?delete a[h]:a.removeAttribute?a.re\
moveAttribute(h):a[h]=null)}},_data:function(a,b,c){return f.data(a,b,c,!0)},ac\
ceptData:function(a){if(a.nodeName){var b=f.noData[a.nodeName.toLowerCase()];if\
(b)return b!==!0&&a.getAttribute(\"classid\")===b}return!0}}),f.fn.extend({data\
:function(a,c){var d,e,g,h,i,j=this[0],k=0,m=null;if(a===b){if(this.length){m=f\
.data(j);if(j.nodeType===1&&!f._data(j,\"parsedAttrs\")){g=j.attributes;for(i=g\
.length;k<i;k++)h=g[k].name,h.indexOf(\"data-\")===0&&(h=f.camelCase(h.substrin\
g(5)),l(j,h,m[h]));f._data(j,\"parsedAttrs\",!0)}}return m}if(typeof a==\"objec\
t\")return this.each(function(){f.data(this,a)});d=a.split(\".\",2),d[1]=d[1]?\
\".\"+d[1]:\"\",e=d[1]+\"!\";return f.access(this,function(c){if(c===b){m=this.\
triggerHandler(\"getData\"+e,[d[0]]),m===b&&j&&(m=f.data(j,a),m=l(j,a,m));retur\
n m===b&&d[1]?this.data(d[0]):m}d[1]=c,this.each(function(){var b=f(this);b.tri\
ggerHandler(\"setData\"+e,d),f.data(this,a,c),b.triggerHandler(\"changeData\"+e\
,d)})},null,c,arguments.length>1,null,!1)},removeData:function(a){return this.e\
ach(function(){f.removeData(this,a)})}}),f.extend({_mark:function(a,b){a&&(b=(b\
||\"fx\")+\"mark\",f._data(a,b,(f._data(a,b)||0)+1))},_unmark:function(a,b,c){a\
!==!0&&(c=b,b=a,a=!1);if(b){c=c||\"fx\";var d=c+\"mark\",e=a?0:(f._data(b,d)||1\
)-1;e?f._data(b,d,e):(f.removeData(b,d,!0),n(b,c,\"mark\"))}},queue:function(a,\
b,c){var d;if(a){b=(b||\"fx\")+\"queue\",d=f._data(a,b),c&&(!d||f.isArray(c)?d=\
f._data(a,b,f.makeArray(c)):d.push(c));return d||[]}},dequeue:function(a,b){b=b\
||\"fx\";var c=f.queue(a,b),d=c.shift(),e={};d===\"inprogress\"&&(d=c.shift()),\
d&&(b===\"fx\"&&c.unshift(\"inprogress\"),f._data(a,b+\".run\",e),d.call(a,func\
tion(){f.dequeue(a,b)},e)),c.length||(f.removeData(a,b+\"queue \"+b+\".run\",!0\
),n(a,b,\"queue\"))}}),f.fn.extend({queue:function(a,c){var d=2;typeof a!=\"str\
ing\"&&(c=a,a=\"fx\",d--);if(arguments.length<d)return f.queue(this[0],a);retur\
n c===b?this:this.each(function(){var b=f.queue(this,a,c);a===\"fx\"&&b[0]!==\"\
inprogress\"&&f.dequeue(this,a)})},dequeue:function(a){return this.each(functio\
n(){f.dequeue(this,a)})},delay:function(a,b){a=f.fx?f.fx.speeds[a]||a:a,b=b||\"\
fx\";return this.queue(b,function(b,c){var d=setTimeout(b,a);c.stop=function(){\
clearTimeout(d)}})},clearQueue:function(a){return this.queue(a||\"fx\",[])},pro\
mise:function(a,c){function m(){--h||d.resolveWith(e,[e])}typeof a!=\"string\"&\
&(c=a,a=b),a=a||\"fx\";var d=f.Deferred(),e=this,g=e.length,h=1,i=a+\"defer\",j\
=a+\"queue\",k=a+\"mark\",l;while(g--)if(l=f.data(e[g],i,b,!0)||(f.data(e[g],j,\
b,!0)||f.data(e[g],k,b,!0))&&f.data(e[g],i,f.Callbacks(\"once memory\"),!0))h++\
,l.add(m);m();return d.promise(c)}});var o=/[\\n\\t\\r]/g,p=/\\s+/,q=/\\r/g,r=/\
^(?:button|input)$/i,s=/^(?:button|input|object|select|textarea)$/i,t=/^a(?:rea\
)?$/i,u=/^(?:autofocus|autoplay|async|checked|controls|defer|disabled|hidden|lo\
op|multiple|open|readonly|required|scoped|selected)$/i,v=f.support.getSetAttrib\
ute,w,x,y;f.fn.extend({attr:function(a,b){return f.access(this,f.attr,a,b,argum\
ents.length>1)},removeAttr:function(a){return this.each(function(){f.removeAttr\
(this,a)})},prop:function(a,b){return f.access(this,f.prop,a,b,arguments.length\
>1)},removeProp:function(a){a=f.propFix[a]||a;return this.each(function(){try{t\
his[a]=b,delete this[a]}catch(c){}})},addClass:function(a){var b,c,d,e,g,h,i;if\
(f.isFunction(a))return this.each(function(b){f(this).addClass(a.call(this,b,th\
is.className))});if(a&&typeof a==\"string\"){b=a.split(p);for(c=0,d=this.length\
;c<d;c++){e=this[c];if(e.nodeType===1)if(!e.className&&b.length===1)e.className\
=a;else{g=\" \"+e.className+\" \";for(h=0,i=b.length;h<i;h++)~g.indexOf(\" \"+b\
[h]+\" \")||(g+=b[h]+\" \");e.className=f.trim(g)}}}return this},removeClass:fu\
nction(a){var c,d,e,g,h,i,j;if(f.isFunction(a))return this.each(function(b){f(t\
his).removeClass(a.call(this,b,this.className))});if(a&&typeof a==\"string\"||a\
===b){c=(a||\"\").split(p);for(d=0,e=this.length;d<e;d++){g=this[d];if(g.nodeTy\
pe===1&&g.className)if(a){h=(\" \"+g.className+\" \").replace(o,\" \");for(i=0,\
j=c.length;i<j;i++)h=h.replace(\" \"+c[i]+\" \",\" \");g.className=f.trim(h)}el\
se g.className=\"\"}}return this},toggleClass:function(a,b){var c=typeof a,d=ty\
peof b==\"boolean\";if(f.isFunction(a))return this.each(function(c){f(this).tog\
gleClass(a.call(this,c,this.className,b),b)});return this.each(function(){if(c=\
==\"string\"){var e,g=0,h=f(this),i=b,j=a.split(p);while(e=j[g++])i=d?i:!h.hasC\
lass(e),h[i?\"addClass\":\"removeClass\"](e)}else if(c===\"undefined\"||c===\"b\
oolean\")this.className&&f._data(this,\"__className__\",this.className),this.cl\
assName=this.className||a===!1?\"\":f._data(this,\"__className__\")||\"\"})},ha\
sClass:function(a){var b=\" \"+a+\" \",c=0,d=this.length;for(;c<d;c++)if(this[c\
].nodeType===1&&(\" \"+this[c].className+\" \").replace(o,\" \").indexOf(b)>-1)\
return!0;return!1},val:function(a){var c,d,e,g=this[0];{if(!!arguments.length){\
e=f.isFunction(a);return this.each(function(d){var g=f(this),h;if(this.nodeType\
===1){e?h=a.call(this,d,g.val()):h=a,h==null?h=\"\":typeof h==\"number\"?h+=\"\
\":f.isArray(h)&&(h=f.map(h,function(a){return a==null?\"\":a+\"\"})),c=f.valHo\
oks[this.type]||f.valHooks[this.nodeName.toLowerCase()];if(!c||!(\"set\"in c)||\
c.set(this,h,\"value\")===b)this.value=h}})}if(g){c=f.valHooks[g.type]||f.valHo\
oks[g.nodeName.toLowerCase()];if(c&&\"get\"in c&&(d=c.get(g,\"value\"))!==b)ret\
urn d;d=g.value;return typeof d==\"string\"?d.replace(q,\"\"):d==null?\"\":d}}}\
}),f.extend({valHooks:{option:{get:function(a){var b=a.attributes.value;return!\
b||b.specified?a.value:a.text}},select:{get:function(a){var b,c,d,e,g=a.selecte\
dIndex,h=[],i=a.options,j=a.type===\"select-one\";if(g<0)return null;c=j?g:0,d=\
j?g+1:i.length;for(;c<d;c++){e=i[c];if(e.selected&&(f.support.optDisabled?!e.di\
sabled:e.getAttribute(\"disabled\")===null)&&(!e.parentNode.disabled||!f.nodeNa\
me(e.parentNode,\"optgroup\"))){b=f(e).val();if(j)return b;h.push(b)}}if(j&&!h.\
length&&i.length)return f(i[g]).val();return h},set:function(a,b){var c=f.makeA\
rray(b);f(a).find(\"option\").each(function(){this.selected=f.inArray(f(this).v\
al(),c)>=0}),c.length||(a.selectedIndex=-1);return c}}},attrFn:{val:!0,css:!0,h\
tml:!0,text:!0,data:!0,width:!0,height:!0,offset:!0},attr:function(a,c,d,e){var\
 g,h,i,j=a.nodeType;if(!!a&&j!==3&&j!==8&&j!==2){if(e&&c in f.attrFn)return f(a\
)[c](d);if(typeof a.getAttribute==\"undefined\")return f.prop(a,c,d);i=j!==1||!\
f.isXMLDoc(a),i&&(c=c.toLowerCase(),h=f.attrHooks[c]||(u.test(c)?x:w));if(d!==b\
){if(d===null){f.removeAttr(a,c);return}if(h&&\"set\"in h&&i&&(g=h.set(a,d,c))!\
==b)return g;a.setAttribute(c,\"\"+d);return d}if(h&&\"get\"in h&&i&&(g=h.get(a\
,c))!==null)return g;g=a.getAttribute(c);return g===null?b:g}},removeAttr:funct\
ion(a,b){var c,d,e,g,h,i=0;if(b&&a.nodeType===1){d=b.toLowerCase().split(p),g=d\
.length;for(;i<g;i++)e=d[i],e&&(c=f.propFix[e]||e,h=u.test(e),h||f.attr(a,e,\"\
\"),a.removeAttribute(v?e:c),h&&c in a&&(a[c]=!1))}},attrHooks:{type:{set:funct\
ion(a,b){if(r.test(a.nodeName)&&a.parentNode)f.error(\"type property can't be c\
hanged\");else if(!f.support.radioValue&&b===\"radio\"&&f.nodeName(a,\"input\")\
){var c=a.value;a.setAttribute(\"type\",b),c&&(a.value=c);return b}}},value:{ge\
t:function(a,b){if(w&&f.nodeName(a,\"button\"))return w.get(a,b);return b in a?\
a.value:null},set:function(a,b,c){if(w&&f.nodeName(a,\"button\"))return w.set(a\
,b,c);a.value=b}}},propFix:{tabindex:\"tabIndex\",readonly:\"readOnly\",\"for\"\
:\"htmlFor\",\"class\":\"className\",maxlength:\"maxLength\",cellspacing:\"cell\
Spacing\",cellpadding:\"cellPadding\",rowspan:\"rowSpan\",colspan:\"colSpan\",u\
semap:\"useMap\",frameborder:\"frameBorder\",contenteditable:\"contentEditable\
\"},prop:function(a,c,d){var e,g,h,i=a.nodeType;if(!!a&&i!==3&&i!==8&&i!==2){h=\
i!==1||!f.isXMLDoc(a),h&&(c=f.propFix[c]||c,g=f.propHooks[c]);return d!==b?g&&\
\"set\"in g&&(e=g.set(a,d,c))!==b?e:a[c]=d:g&&\"get\"in g&&(e=g.get(a,c))!==nul\
l?e:a[c]}},propHooks:{tabIndex:{get:function(a){var c=a.getAttributeNode(\"tabi\
ndex\");return c&&c.specified?parseInt(c.value,10):s.test(a.nodeName)||t.test(a\
.nodeName)&&a.href?0:b}}}}),f.attrHooks.tabindex=f.propHooks.tabIndex,x={get:fu\
nction(a,c){var d,e=f.prop(a,c);return e===!0||typeof e!=\"boolean\"&&(d=a.getA\
ttributeNode(c))&&d.nodeValue!==!1?c.toLowerCase():b},set:function(a,b,c){var d\
;b===!1?f.removeAttr(a,c):(d=f.propFix[c]||c,d in a&&(a[d]=!0),a.setAttribute(c\
,c.toLowerCase()));return c}},v||(y={name:!0,id:!0,coords:!0},w=f.valHooks.butt\
on={get:function(a,c){var d;d=a.getAttributeNode(c);return d&&(y[c]?d.nodeValue\
!==\"\":d.specified)?d.nodeValue:b},set:function(a,b,d){var e=a.getAttributeNod\
e(d);e||(e=c.createAttribute(d),a.setAttributeNode(e));return e.nodeValue=b+\"\
\"}},f.attrHooks.tabindex.set=w.set,f.each([\"width\",\"height\"],function(a,b)\
{f.attrHooks[b]=f.extend(f.attrHooks[b],{set:function(a,c){if(c===\"\"){a.setAt\
tribute(b,\"auto\");return c}}})}),f.attrHooks.contenteditable={get:w.get,set:f\
unction(a,b,c){b===\"\"&&(b=\"false\"),w.set(a,b,c)}}),f.support.hrefNormalized\
||f.each([\"href\",\"src\",\"width\",\"height\"],function(a,c){f.attrHooks[c]=f\
.extend(f.attrHooks[c],{get:function(a){var d=a.getAttribute(c,2);return d===nu\
ll?b:d}})}),f.support.style||(f.attrHooks.style={get:function(a){return a.style\
.cssText.toLowerCase()||b},set:function(a,b){return a.style.cssText=\"\"+b}}),f\
.support.optSelected||(f.propHooks.selected=f.extend(f.propHooks.selected,{get:\
function(a){var b=a.parentNode;b&&(b.selectedIndex,b.parentNode&&b.parentNode.s\
electedIndex);return null}})),f.support.enctype||(f.propFix.enctype=\"encoding\
\"),f.support.checkOn||f.each([\"radio\",\"checkbox\"],function(){f.valHooks[th\
is]={get:function(a){return a.getAttribute(\"value\")===null?\"on\":a.value}}})\
,f.each([\"radio\",\"checkbox\"],function(){f.valHooks[this]=f.extend(f.valHook\
s[this],{set:function(a,b){if(f.isArray(b))return a.checked=f.inArray(f(a).val(\
),b)>=0}})});var z=/^(?:textarea|input|select)$/i,A=/^([^\\.]*)?(?:\\.(.+))?$/,\
B=/(?:^|\\s)hover(\\.\\S+)?\\b/,C=/^key/,D=/^(?:mouse|contextmenu)|click/,E=/^(\
?:focusinfocus|focusoutblur)$/,F=/^(\\w*)(?:#([\\w\\-]+))?(?:\\.([\\w\\-]+))?$/\
,G=function( a){var b=F.exec(a);b&&(b[1]=(b[1]||\"\").toLowerCase(),b[3]=b[3]&&\
new RegExp(\"(?:^|\\\\s)\"+b[3]+\"(?:\\\\s|$)\"));return b},H=function(a,b){var\
 c=a.attributes||{};return(!b[1]||a.nodeName.toLowerCase()===b[1])&&(!b[2]||(c.\
id||{}).value===b[2])&&(!b[3]||b[3].test((c[\"class\"]||{}).value))},I=function\
(a){return f.event.special.hover?a:a.replace(B,\"mouseenter$1 mouseleave$1\")};\
f.event={add:function(a,c,d,e,g){var h,i,j,k,l,m,n,o,p,q,r,s;if(!(a.nodeType===\
3||a.nodeType===8||!c||!d||!(h=f._data(a)))){d.handler&&(p=d,d=p.handler,g=p.se\
lector),d.guid||(d.guid=f.guid++),j=h.events,j||(h.events=j={}),i=h.handle,i||(\
h.handle=i=function(a){return typeof f!=\"undefined\"&&(!a||f.event.triggered!=\
=a.type)?f.event.dispatch.apply(i.elem,arguments):b},i.elem=a),c=f.trim(I(c)).s\
plit(\" \");for(k=0;k<c.length;k++){l=A.exec(c[k])||[],m=l[1],n=(l[2]||\"\").sp\
lit(\".\").sort(),s=f.event.special[m]||{},m=(g?s.delegateType:s.bindType)||m,s\
=f.event.special[m]||{},o=f.extend({type:m,origType:l[1],data:e,handler:d,guid:\
d.guid,selector:g,quick:g&&G(g),namespace:n.join(\".\")},p),r=j[m];if(!r){r=j[m\
]=[],r.delegateCount=0;if(!s.setup||s.setup.call(a,e,n,i)===!1)a.addEventListen\
er?a.addEventListener(m,i,!1):a.attachEvent&&a.attachEvent(\"on\"+m,i)}s.add&&(\
s.add.call(a,o),o.handler.guid||(o.handler.guid=d.guid)),g?r.splice(r.delegateC\
ount++,0,o):r.push(o),f.event.global[m]=!0}a=null}},global:{},remove:function(a\
,b,c,d,e){var g=f.hasData(a)&&f._data(a),h,i,j,k,l,m,n,o,p,q,r,s;if(!!g&&!!(o=g\
.events)){b=f.trim(I(b||\"\")).split(\" \");for(h=0;h<b.length;h++){i=A.exec(b[\
h])||[],j=k=i[1],l=i[2];if(!j){for(j in o)f.event.remove(a,j+b[h],c,d,!0);conti\
nue}p=f.event.special[j]||{},j=(d?p.delegateType:p.bindType)||j,r=o[j]||[],m=r.\
length,l=l?new RegExp(\"(^|\\\\.)\"+l.split(\".\").sort().join(\"\\\\.(?:.*\
\\\\.)?\")+\"(\\\\.|$)\"):null;for(n=0;n<r.length;n++)s=r[n],(e||k===s.origType\
)&&(!c||c.guid===s.guid)&&(!l||l.test(s.namespace))&&(!d||d===s.selector||d===\
\"**\"&&s.selector)&&(r.splice(n--,1),s.selector&&r.delegateCount--,p.remove&&p\
.remove.call(a,s));r.length===0&&m!==r.length&&((!p.teardown||p.teardown.call(a\
,l)===!1)&&f.removeEvent(a,j,g.handle),delete o[j])}f.isEmptyObject(o)&&(q=g.ha\
ndle,q&&(q.elem=null),f.removeData(a,[\"events\",\"handle\"],!0))}},customEvent\
:{getData:!0,setData:!0,changeData:!0},trigger:function(c,d,e,g){if(!e||e.nodeT\
ype!==3&&e.nodeType!==8){var h=c.type||c,i=[],j,k,l,m,n,o,p,q,r,s;if(E.test(h+f\
.event.triggered))return;h.indexOf(\"!\")>=0&&(h=h.slice(0,-1),k=!0),h.indexOf(\
\".\")>=0&&(i=h.split(\".\"),h=i.shift(),i.sort());if((!e||f.event.customEvent[\
h])&&!f.event.global[h])return;c=typeof c==\"object\"?c[f.expando]?c:new f.Even\
t(h,c):new f.Event(h),c.type=h,c.isTrigger=!0,c.exclusive=k,c.namespace=i.join(\
\".\"),c.namespace_re=c.namespace?new RegExp(\"(^|\\\\.)\"+i.join(\"\\\\.(?:.*\
\\\\.)?\")+\"(\\\\.|$)\"):null,o=h.indexOf(\":\")<0?\"on\"+h:\"\";if(!e){j=f.ca\
che;for(l in j)j[l].events&&j[l].events[h]&&f.event.trigger(c,d,j[l].handle.ele\
m,!0);return}c.result=b,c.target||(c.target=e),d=d!=null?f.makeArray(d):[],d.un\
shift(c),p=f.event.special[h]||{};if(p.trigger&&p.trigger.apply(e,d)===!1)retur\
n;r=[[e,p.bindType||h]];if(!g&&!p.noBubble&&!f.isWindow(e)){s=p.delegateType||h\
,m=E.test(s+h)?e:e.parentNode,n=null;for(;m;m=m.parentNode)r.push([m,s]),n=m;n&\
&n===e.ownerDocument&&r.push([n.defaultView||n.parentWindow||a,s])}for(l=0;l<r.\
length&&!c.isPropagationStopped();l++)m=r[l][0],c.type=r[l][1],q=(f._data(m,\"e\
vents\")||{})[c.type]&&f._data(m,\"handle\"),q&&q.apply(m,d),q=o&&m[o],q&&f.acc\
eptData(m)&&q.apply(m,d)===!1&&c.preventDefault();c.type=h,!g&&!c.isDefaultPrev\
ented()&&(!p._default||p._default.apply(e.ownerDocument,d)===!1)&&(h!==\"click\
\"||!f.nodeName(e,\"a\"))&&f.acceptData(e)&&o&&e[h]&&(h!==\"focus\"&&h!==\"blur\
\"||c.target.offsetWidth!==0)&&!f.isWindow(e)&&(n=e[o],n&&(e[o]=null),f.event.t\
riggered=h,e[h](),f.event.triggered=b,n&&(e[o]=n));return c.result}},dispatch:f\
unction(c){c=f.event.fix(c||a.event);var d=(f._data(this,\"events\")||{})[c.typ\
e]||[],e=d.delegateCount,g=[].slice.call(arguments,0),h=!c.exclusive&&!c.namesp\
ace,i=f.event.special[c.type]||{},j=[],k,l,m,n,o,p,q,r,s,t,u;g[0]=c,c.delegateT\
arget=this;if(!i.preDispatch||i.preDispatch.call(this,c)!==!1){if(e&&(!c.button\
||c.type!==\"click\")){n=f(this),n.context=this.ownerDocument||this;for(m=c.tar\
get;m!=this;m=m.parentNode||this)if(m.disabled!==!0){p={},r=[],n[0]=m;for(k=0;k\
<e;k++)s=d[k],t=s.selector,p[t]===b&&(p[t]=s.quick?H(m,s.quick):n.is(t)),p[t]&&\
r.push(s);r.length&&j.push({elem:m,matches:r})}}d.length>e&&j.push({elem:this,m\
atches:d.slice(e)});for(k=0;k<j.length&&!c.isPropagationStopped();k++){q=j[k],c\
.currentTarget=q.elem;for(l=0;l<q.matches.length&&!c.isImmediatePropagationStop\
ped();l++){s=q.matches[l];if(h||!c.namespace&&!s.namespace||c.namespace_re&&c.n\
amespace_re.test(s.namespace))c.data=s.data,c.handleObj=s,o=((f.event.special[s\
.origType]||{}).handle||s.handler).apply(q.elem,g),o!==b&&(c.result=o,o===!1&&(\
c.preventDefault(),c.stopPropagation()))}}i.postDispatch&&i.postDispatch.call(t\
his,c);return c.result}},props:\"attrChange attrName relatedNode srcElement alt\
Key bubbles cancelable ctrlKey currentTarget eventPhase metaKey relatedTarget s\
hiftKey target timeStamp view which\".split(\" \"),fixHooks:{},keyHooks:{props:\
\"char charCode key keyCode\".split(\" \"),filter:function(a,b){a.which==null&&\
(a.which=b.charCode!=null?b.charCode:b.keyCode);return a}},mouseHooks:{props:\"\
button buttons clientX clientY fromElement offsetX offsetY pageX pageY screenX \
screenY toElement\".split(\" \"),filter:function(a,d){var e,f,g,h=d.button,i=d.\
fromElement;a.pageX==null&&d.clientX!=null&&(e=a.target.ownerDocument||c,f=e.do\
cumentElement,g=e.body,a.pageX=d.clientX+(f&&f.scrollLeft||g&&g.scrollLeft||0)-\
(f&&f.clientLeft||g&&g.clientLeft||0),a.pageY=d.clientY+(f&&f.scrollTop||g&&g.s\
crollTop||0)-(f&&f.clientTop||g&&g.clientTop||0)),!a.relatedTarget&&i&&(a.relat\
edTarget=i===a.target?d.toElement:i),!a.which&&h!==b&&(a.which=h&1?1:h&2?3:h&4?\
2:0);return a}},fix:function(a){if(a[f.expando])return a;var d,e,g=a,h=f.event.\
fixHooks[a.type]||{},i=h.props?this.props.concat(h.props):this.props;a=f.Event(\
g);for(d=i.length;d;)e=i[--d],a[e]=g[e];a.target||(a.target=g.srcElement||c),a.\
target.nodeType===3&&(a.target=a.target.parentNode),a.metaKey===b&&(a.metaKey=a\
.ctrlKey);return h.filter?h.filter(a,g):a},special:{ready:{setup:f.bindReady},l\
oad:{noBubble:!0},focus:{delegateType:\"focusin\"},blur:{delegateType:\"focusou\
t\"},beforeunload:{setup:function(a,b,c){f.isWindow(this)&&(this.onbeforeunload\
=c)},teardown:function(a,b){this.onbeforeunload===b&&(this.onbeforeunload=null)\
}}},simulate:function(a,b,c,d){var e=f.extend(new f.Event,c,{type:a,isSimulated\
:!0,originalEvent:{}});d?f.event.trigger(e,null,b):f.event.dispatch.call(b,e),e\
.isDefaultPrevented()&&c.preventDefault()}},f.event.handle=f.event.dispatch,f.r\
emoveEvent=c.removeEventListener?function(a,b,c){a.removeEventListener&&a.remov\
eEventListener(b,c,!1)}:function(a,b,c){a.detachEvent&&a.detachEvent(\"on\"+b,c\
)},f.Event=function(a,b){if(!(this instanceof f.Event))return new f.Event(a,b);\
a&&a.type?(this.originalEvent=a,this.type=a.type,this.isDefaultPrevented=a.defa\
ultPrevented||a.returnValue===!1||a.getPreventDefault&&a.getPreventDefault()?K:\
J):this.type=a,b&&f.extend(this,b),this.timeStamp=a&&a.timeStamp||f.now(),this[\
f.expando]=!0},f.Event.prototype={preventDefault:function(){this.isDefaultPreve\
nted=K;var a=this.originalEvent;!a||(a.preventDefault?a.preventDefault():a.retu\
rnValue=!1)},stopPropagation:function(){this.isPropagationStopped=K;var a=this.\
originalEvent;!a||(a.stopPropagation&&a.stopPropagation(),a.cancelBubble=!0)},s\
topImmediatePropagation:function(){this.isImmediatePropagationStopped=K,this.st\
opPropagation()},isDefaultPrevented:J,isPropagationStopped:J,isImmediatePropaga\
tionStopped:J},f.each({mouseenter:\"mouseover\",mouseleave:\"mouseout\"},functi\
on(a,b){f.event.special[a]={delegateType:b,bindType:b,handle:function(a){var c=\
this,d=a.relatedTarget,e=a.handleObj,g=e.selector,h;if(!d||d!==c&&!f.contains(c\
,d))a.type=e.origType,h=e.handler.apply(this,arguments),a.type=b;return h}}}),f\
.support.submitBubbles||(f.event.special.submit={setup:function(){if(f.nodeName\
(this,\"form\"))return!1;f.event.add(this,\"click._submit keypress._submit\",fu\
nction(a){var c=a.target,d=f.nodeName(c,\"input\")||f.nodeName(c,\"button\")?c.\
form:b;d&&!d._submit_attached&&(f.event.add(d,\"submit._submit\",function(a){a.\
_submit_bubble=!0}),d._submit_attached=!0)})},postDispatch:function(a){a._submi\
t_bubble&&(delete a._submit_bubble,this.parentNode&&!a.isTrigger&&f.event.simul\
ate(\"submit\",this.parentNode,a,!0))},teardown:function(){if(f.nodeName(this,\
\"form\"))return!1;f.event.remove(this,\"._submit\")}}),f.support.changeBubbles\
||(f.event.special.change={setup:function(){if(z.test(this.nodeName)){if(this.t\
ype===\"checkbox\"||this.type===\"radio\")f.event.add(this,\"propertychange._ch\
ange\",function(a){a.originalEvent.propertyName===\"checked\"&&(this._just_chan\
ged=!0)}),f.event.add(this,\"click._change\",function(a){this._just_changed&&!a\
.isTrigger&&(this._just_changed=!1,f.event.simulate(\"change\",this,a,!0))});re\
turn!1}f.event.add(this,\"beforeactivate._change\",function(a){var b=a.target;z\
.test(b.nodeName)&&!b._change_attached&&(f.event.add(b,\"change._change\",funct\
ion(a){this.parentNode&&!a.isSimulated&&!a.isTrigger&&f.event.simulate(\"change\
\",this.parentNode,a,!0)}),b._change_attached=!0)})},handle:function(a){var b=a\
.target;if(this!==b||a.isSimulated||a.isTrigger||b.type!==\"radio\"&&b.type!==\
\"checkbox\")return a.handleObj.handler.apply(this,arguments)},teardown:functio\
n(){f.event.remove(this,\"._change\");return z.test(this.nodeName)}}),f.support\
.focusinBubbles||f.each({focus:\"focusin\",blur:\"focusout\"},function(a,b){var\
 d=0,e=function(a){f.event.simulate(b,a.target,f.event.fix(a),!0)};f.event.spec\
ial[b]={setup:function(){d++===0&&c.addEventListener(a,e,!0)},teardown:function\
(){--d===0&&c.removeEventListener(a,e,!0)}}}),f.fn.extend({on:function(a,c,d,e,\
g){var h,i;if(typeof a==\"object\"){typeof c!=\"string\"&&(d=d||c,c=b);for(i in\
 a)this.on(i,c,d,a[i],g);return this}d==null&&e==null?(e=c,d=c=b):e==null&&(typ\
eof c==\"string\"?(e=d,d=b):(e=d,d=c,c=b));if(e===!1)e=J;else if(!e)return this\
;g===1&&(h=e,e=function(a){f().off(a);return h.apply(this,arguments)},e.guid=h.\
guid||(h.guid=f.guid++));return this.each(function(){f.event.add(this,a,e,d,c)}\
)},one:function(a,b,c,d){return this.on(a,b,c,d,1)},off:function(a,c,d){if(a&&a\
.preventDefault&&a.handleObj){var e=a.handleObj;f(a.delegateTarget).off(e.names\
pace?e.origType+\".\"+e.namespace:e.origType,e.selector,e.handler);return this}\
if(typeof a==\"object\"){for(var g in a)this.off(g,c,a[g]);return this}if(c===!\
1||typeof c==\"function\")d=c,c=b;d===!1&&(d=J);return this.each(function(){f.e\
vent.remove(this,a,d,c)})},bind:function(a,b,c){return this.on(a,null,b,c)},unb\
ind:function(a,b){return this.off(a,null,b)},live:function(a,b,c){f(this.contex\
t).on(a,this.selector,b,c);return this},die:function(a,b){f(this.context).off(a\
,this.selector||\"**\",b);return this},delegate:function(a,b,c,d){return this.o\
n(b,a,c,d)},undelegate:function(a,b,c){return arguments.length==1?this.off(a,\"\
**\"):this.off(b,a,c)},trigger:function(a,b){return this.each(function(){f.even\
t.trigger(a,b,this)})},triggerHandler:function(a,b){if(this[0])return f.event.t\
rigger(a,b,this[0],!0)},toggle:function(a){var b=arguments,c=a.guid||f.guid++,d\
=0,e=function(c){var e=(f._data(this,\"lastToggle\"+a.guid)||0)%d;f._data(this,\
\"lastToggle\"+a.guid,e+1),c.preventDefault();return b[e].apply(this,arguments)\
||!1};e.guid=c;while(d<b.length)b[d++].guid=c;return this.click(e)},hover:funct\
ion(a,b){return this.mouseenter(a).mouseleave(b||a)}}),f.each(\"blur focus focu\
sin focusout load resize scroll unload click dblclick mousedown mouseup mousemo\
ve mouseover mouseout mouseenter mouseleave change select submit keydown keypre\
ss keyup error contextmenu\".split(\" \"),function(a,b){f.fn[b]=function(a,c){c\
==null&&(c=a,a=null);return arguments.length>0?this.on(b,null,a,c):this.trigger\
(b)},f.attrFn&&(f.attrFn[b]=!0),C.test(b)&&(f.event.fixHooks[b]=f.event.keyHook\
s),D.test(b)&&(f.event.fixHooks[b]=f.event.mouseHooks)}),function(){function x(\
a,b,c,e,f,g){for(var h=0,i=e.length;h<i;h++){var j=e[h];if(j){var k=!1;j=j[a];w\
hile(j){if(j[d]===c){k=e[j.sizset];break}if(j.nodeType===1){g||(j[d]=c,j.sizset\
=h);if(typeof b!=\"string\"){if(j===b){k=!0;break}}else if(m.filter(b,[j]).leng\
th>0){k=j;break}}j=j[a]}e[h]=k}}}function w(a,b,c,e,f,g){for(var h=0,i=e.length\
;h<i;h++){var j=e[h];if(j){var k=!1;j=j[a];while(j){if(j[d]===c){k=e[j.sizset];\
break}j.nodeType===1&&!g&&(j[d]=c,j.sizset=h);if(j.nodeName.toLowerCase()===b){\
k=j;break}j=j[a]}e[h]=k}}}var a=/((?:\\((?:\\([^()]+\\)|[^()]+)+\\)|\\[(?:\\[[^\
\\[\\]]*\\]|['\"][^'\"]*['\"]|[^\\[\\]'\"]+)+\\]|\\\\.|[^ >+~,(\\[\\\\]+)+|[>+~\
])(\\s*,\\s*)?((?:.|\\r|\\n)*)/g,d=\"sizcache\"+(Math.random()+\"\").replace(\"\
.\",\"\"),e=0,g=Object.prototype.toString,h=!1,i=!0,j=/\\\\/g,k=/\\r\\n/g,l=/\
\\W/;[0,0].sort(function(){i=!1;return 0});var m=function(b,d,e,f){e=e||[],d=d|\
|c;var h=d;if(d.nodeType!==1&&d.nodeType!==9)return[];if(!b||typeof b!=\"string\
\")return e;var i,j,k,l,n,q,r,t,u=!0,v=m.isXML(d),w=[],x=b;do{a.exec(\"\"),i=a.\
exec(x);if(i){x=i[3],w.push(i[1]);if(i[2]){l=i[3];break}}}while(i);if(w.length>\
1&&p.exec(b))if(w.length===2&&o.relative[w[0]])j=y(w[0]+w[1],d,f);else{j=o.rela\
tive[w[0]]?[d]:m(w.shift(),d);while(w.length)b=w.shift(),o.relative[b]&&(b+=w.s\
hift()),j=y(b,j,f)}else{!f&&w.length>1&&d.nodeType===9&&!v&&o.match.ID.test(w[0\
])&&!o.match.ID.test(w[w.length-1])&&(n=m.find(w.shift(),d,v),d=n.expr?m.filter\
(n.expr,n.set)[0]:n.set[0]);if(d){n=f?{expr:w.pop(),set:s(f)}:m.find(w.pop(),w.\
length===1&&(w[0]===\"~\"||w[0]===\"+\")&&d.parentNode?d.parentNode:d,v),j=n.ex\
pr?m.filter(n.expr,n.set):n.set,w.length>0?k=s(j):u=!1;while(w.length)q=w.pop()\
,r=q,o.relative[q]?r=w.pop():q=\"\",r==null&&(r=d),o.relative[q](k,r,v)}else k=\
w=[]}k||(k=j),k||m.error(q||b);if(g.call(k)===\"[object Array]\")if(!u)e.push.a\
pply(e,k);else if(d&&d.nodeType===1)for(t=0;k[t]!=null;t++)k[t]&&(k[t]===!0||k[\
t].nodeType===1&&m.contains(d,k[t]))&&e.push(j[t]);else for(t=0;k[t]!=null;t++)\
k[t]&&k[t].nodeType===1&&e.push(j[t]);else s(k,e);l&&(m(l,h,e,f),m.uniqueSort(e\
));return e};m.uniqueSort=function(a){if(u){h=i,a.sort(u);if(h)for(var b=1;b<a.\
length;b++)a[b]===a[b-1]&&a.splice(b--,1)}return a},m.matches=function(a,b){ret\
urn m(a,null,null,b)},m.matchesSelector=function(a,b){return m(b,null,null,[a])\
.length>0},m.find=function(a,b,c){var d,e,f,g,h,i;if(!a)return[];for(e=0,f=o.or\
der.length;e<f;e++){h=o.order[e];if(g=o.leftMatch[h].exec(a)){i=g[1],g.splice(1\
,1);if(i.substr(i.length-1)!==\"\\\\\"){g[1]=(g[1]||\"\").replace(j,\"\"),d=o.f\
ind[h](g,b,c);if(d!=null){a=a.replace(o.match[h],\"\");break}}}}d||(d=typeof b.\
getElementsByTagName!=\"undefined\"?b.getElementsByTagName(\"*\"):[]);return{se\
t:d,expr:a}},m.filter=function(a,c,d,e){var f,g,h,i,j,k,l,n,p,q=a,r=[],s=c,t=c&\
&c[0]&&m.isXML(c[0]);while(a&&c.length){for(h in o.filter)if((f=o.leftMatch[h].\
exec(a))!=null&&f[2]){k=o.filter[h],l=f[1],g=!1,f.splice(1,1);if(l.substr(l.len\
gth-1)===\"\\\\\")continue;s===r&&(r=[]);if(o.preFilter[h]){f=o.preFilter[h](f,\
s,d,r,e,t);if(!f)g=i=!0;else if(f===!0)continue}if(f)for(n=0;(j=s[n])!=null;n++\
)j&&(i=k(j,f,n,s),p=e^i,d&&i!=null?p?g=!0:s[n]=!1:p&&(r.push(j),g=!0));if(i!==b\
){d||(s=r),a=a.replace(o.match[h],\"\");if(!g)return[];break}}if(a===q)if(g==nu\
ll)m.error(a);else break;q=a}return s},m.error=function(a){throw new Error(\"Sy\
ntax error, unrecognized expression: \"+a)};var n=m.getText=function(a){var b,c\
,d=a.nodeType,e=\"\";if(d){if(d===1||d===9||d===11){if(typeof a.textContent==\"\
string\")return a.textContent;if(typeof a.innerText==\"string\")return a.innerT\
ext.replace(k,\"\");for(a=a.firstChild;a;a=a.nextSibling)e+=n(a)}else if(d===3|\
|d===4)return a.nodeValue}else for(b=0;c=a[b];b++)c.nodeType!==8&&(e+=n(c));ret\
urn e},o=m.selectors={order:[\"ID\",\"NAME\",\"TAG\"],match:{ID:/#((?:[\\w\\u00\
c0-\\uFFFF\\-]|\\\\.)+)/,CLASS:/\\.((?:[\\w\\u00c0-\\uFFFF\\-]|\\\\.)+)/,NAME:/\
\\[name=['\"]*((?:[\\w\\u00c0-\\uFFFF\\-]|\\\\.)+)['\"]*\\]/,ATTR:/\\[\\s*((?:[\
\\w\\u00c0-\\uFFFF\\-]|\\\\.)+)\\s*(?:(\\S?=)\\s*(?:(['\"])(.*?)\\3|(#?(?:[\\w\
\\u00c0-\\uFFFF\\-]|\\\\.)*)|)|)\\s*\\]/,TAG:/^((?:[\\w\\u00c0-\\uFFFF\\*\\-]|\
\\\\.)+)/,CHILD:/:(only|nth|last|first)-child(?:\\(\\s*(even|odd|(?:[+\\-]?\\d+\
|(?:[+\\-]?\\d*)?n\\s*(?:[+\\-]\\s*\\d+)?))\\s*\\))?/,POS:/:(nth|eq|gt|lt|first\
|last|even|odd)(?:\\((\\d*)\\))?(?=[^\\-]|$)/,PSEUDO:/:((?:[\\w\\u00c0-\\uFFFF\
\\-]|\\\\.)+)(?:\\((['\"]?)((?:\\([^\\)]+\\)|[^\\(\\)]*)+)\\2\\))?/},leftMatch:\
{},attrMap:{\"class\":\"className\",\"for\":\"htmlFor\"},attrHandle:{href:funct\
ion(a){return a.getAttribute(\"href\")},type:function(a){return a.getAttribute(\
\"type\")}},relative:{\"+\":function(a,b){var c=typeof b==\"string\",d=c&&!l.te\
st(b),e=c&&!d;d&&(b=b.toLowerCase());for(var f=0,g=a.length,h;f<g;f++)if(h=a[f]\
){while((h=h.previousSibling)&&h.nodeType!==1);a[f]=e||h&&h.nodeName.toLowerCas\
e()===b?h||!1:h===b}e&&m.filter(b,a,!0)},\">\":function(a,b){var c,d=typeof b==\
\"string\",e=0,f=a.length;if(d&&!l.test(b)){b=b.toLowerCase();for(;e<f;e++){c=a\
[e];if(c){var g=c.parentNode;a[e]=g.nodeName.toLowerCase()===b?g:!1}}}else{for(\
;e<f;e++)c=a[e],c&&(a[e]=d?c.parentNode:c.parentNode===b);d&&m.filter(b,a,!0)}}\
,\"\":function(a,b,c){var d,f=e++,g=x;typeof b==\"string\"&&!l.test(b)&&(b=b.to\
LowerCase(),d=b,g=w),g(\"parentNode\",b,f,a,d,c)},\"~\":function(a,b,c){var d,f\
=e++,g=x;typeof b==\"string\"&&!l.test(b)&&(b=b.toLowerCase(),d=b,g=w),g(\"prev\
iousSibling\",b,f,a,d,c)}},find:{ID:function(a,b,c){if(typeof b.getElementById!\
=\"undefined\"&&!c){var d=b.getElementById(a[1]);return d&&d.parentNode?[d]:[]}\
},NAME:function(a,b){if(typeof b.getElementsByName!=\"undefined\"){var c=[],d=b\
.getElementsByName(a[1]);for(var e=0,f=d.length;e<f;e++)d[e].getAttribute(\"nam\
e\")===a[1]&&c.push(d[e]);return c.length===0?null:c}},TAG:function(a,b){if(typ\
eof b.getElementsByTagName!=\"undefined\")return b.getElementsByTagName(a[1])}}\
,preFilter:{CLASS:function(a,b,c,d,e,f){a=\" \"+a[1].replace(j,\"\")+\" \";if(f\
)return a;for(var g=0,h;(h=b[g])!=null;g++)h&&(e^(h.className&&(\" \"+h.classNa\
me+\" \").replace(/[\\t\\n\\r]/g,\" \").indexOf(a)>=0)?c||d.push(h):c&&(b[g]=!1\
));return!1},ID:function(a){return a[1].replace(j,\"\")},TAG:function(a,b){retu\
rn a[1].replace(j,\"\").toLowerCase()},CHILD:function(a){if(a[1]===\"nth\"){a[2\
]||m.error(a[0]),a[2]=a[2].replace(/^\\+|\\s*/g,\"\");var b=/(-?)(\\d*)(?:n([+\
\\-]?\\d*))?/.exec(a[2]===\"even\"&&\"2n\"||a[2]===\"odd\"&&\"2n+1\"||!/\\D/.te\
st(a[2])&&\"0n+\"+a[2]||a[2]);a[2]=b[1]+(b[2]||1)-0,a[3]=b[3]-0}else a[2]&&m.er\
ror(a[0]);a[0]=e++;return a},ATTR:function(a,b,c,d,e,f){var g=a[1]=a[1].replace\
(j,\"\");!f&&o.attrMap[g]&&(a[1]=o.attrMap[g]),a[4]=(a[4]||a[5]||\"\").replace(\
j,\"\"),a[2]===\"~=\"&&(a[4]=\" \"+a[4]+\" \");return a},PSEUDO:function(b,c,d,\
e,f){if(b[1]===\"not\")if((a.exec(b[3])||\"\").length>1||/^\\w/.test(b[3]))b[3]\
=m(b[3],null,null,c);else{var g=m.filter(b[3],c,d,!0^f);d||e.push.apply(e,g);re\
turn!1}else if(o.match.POS.test(b[0])||o.match.CHILD.test(b[0]))return!0;return\
 b},POS:function(a){a.unshift(!0);return a}},filters:{enabled:function(a){retur\
n a.disabled===!1&&a.type!==\"hidden\"},disabled:function(a){return a.disabled=\
==!0},checked:function(a){return a.checked===!0},selected:function(a){a.parentN\
ode&&a.parentNode.selectedIndex;return a.selected===!0},parent:function(a){retu\
rn!!a.firstChild},empty:function(a){return!a.firstChild},has:function(a,b,c){re\
turn!!m(c[3],a).length},header:function(a){return/h\\d/i.test(a.nodeName)},text\
:function(a){var b=a.getAttribute(\"type\"),c=a.type;return a.nodeName.toLowerC\
ase()===\"input\"&&\"text\"===c&&(b===c||b===null)},radio:function(a){return a.\
nodeName.toLowerCase()===\"input\"&&\"radio\"===a.type},checkbox:function(a){re\
turn a.nodeName.toLowerCase()===\"input\"&&\"checkbox\"===a.type},file:function\
(a){return a.nodeName.toLowerCase()===\"input\"&&\"file\"===a.type},password:fu\
nction(a){return a.nodeName.toLowerCase()===\"input\"&&\"password\"===a.type},s\
ubmit:function(a){var b=a.nodeName.toLowerCase();return(b===\"input\"||b===\"bu\
tton\")&&\"submit\"===a.type},image:function(a){return a.nodeName.toLowerCase()\
===\"input\"&&\"image\"===a.type},reset:function(a){var b=a.nodeName.toLowerCas\
e();return(b===\"input\"||b===\"button\")&&\"reset\"===a.type},button:function(\
a){var b=a.nodeName.toLowerCase();return b===\"input\"&&\"button\"===a.type||b=\
==\"button\"},input:function(a){return/input|select|textarea|button/i.test(a.no\
deName)},focus:function(a){return a===a.ownerDocument.activeElement}},setFilter\
s:{first:function(a,b){return b===0},last:function(a,b,c,d){return b===d.length\
-1},even:function(a,b){return b%2===0},odd:function(a,b){return b%2===1},lt:fun\
ction(a,b,c){return b<c[3]-0},gt:function(a,b,c){return b>c[3]-0},nth:function(\
a,b,c){return c[3]-0===b},eq:function(a,b,c){return c[3]-0===b}},filter:{PSEUDO\
:function(a,b,c,d){var e=b[1],f=o.filters[e];if(f)return f(a,c,b,d);if(e===\"co\
ntains\")return(a.textContent||a.innerText||n([a])||\"\").indexOf(b[3])>=0;if(e\
===\"not\"){var g=b[3];for(var h=0,i=g.length;h<i;h++)if(g[h]===a)return!1;retu\
rn!0}m.error(e)},CHILD:function(a,b){var c,e,f,g,h,i,j,k=b[1],l=a;switch(k){cas\
e\"only\":case\"first\":while(l=l.previousSibling)if(l.nodeType===1)return!1;if\
(k===\"first\")return!0;l=a;case\"last\":while(l=l.nextSibling)if(l.nodeType===\
1)return!1;return!0;case\"nth\":c=b[2],e=b[3];if(c===1&&e===0)return!0;f=b[0],g\
=a.parentNode;if(g&&(g[d]!==f||!a.nodeIndex)){i=0;for(l=g.firstChild;l;l=l.next\
Sibling)l.nodeType===1&&(l.nodeIndex=++i);g[d]=f}j=a.nodeIndex-e;return c===0?j\
===0:j%c===0&&j/c>=0}},ID:function(a,b){return a.nodeType===1&&a.getAttribute(\
\"id\")===b},TAG:function(a,b){return b===\"*\"&&a.nodeType===1||!!a.nodeName&&\
a.nodeName.toLowerCase()===b},CLASS:function(a,b){return(\" \"+(a.className||a.\
getAttribute(\"class\"))+\" \").indexOf(b)>-1},ATTR:function(a,b){var c=b[1],d=\
m.attr?m.attr(a,c):o.attrHandle[c]?o.attrHandle[c](a):a[c]!=null?a[c]:a.getAttr\
ibute(c),e=d+\"\",f=b[2],g=b[4];return d==null?f===\"!=\":!f&&m.attr?d!=null:f=\
==\"=\"?e===g:f===\"*=\"?e.indexOf(g)>=0:f===\"~=\"?(\" \"+e+\" \").indexOf(g)>\
=0:g?f===\"!=\"?e!==g:f===\"^=\"?e.indexOf(g)===0:f===\"$=\"?e.substr(e.length-\
g.length)===g:f===\"|=\"?e===g||e.substr(0,g.length+1)===g+\"-\":!1:e&&d!==!1},\
POS:function(a,b,c,d){var e=b[2],f=o.setFilters[e];if(f)return f(a,c,b,d)}}},p=\
o.match.POS,q=function(a,b){return\"\\\\\"+(b-0+1)};for(var r in o.match)o.matc\
h[r]=new RegExp(o.match[r].source+/(?![^\\[]*\\])(?![^\\(]*\\))/.source),o.left\
Match[r]=new RegExp(/(^(?:.|\\r|\\n)*?)/.source+o.match[r].source.replace(/\
\\\\(\\d+)/g,q));o.match.globalPOS=p;var s=function(a,b){a=Array.prototype.slic\
e.call(a,0);if(b){b.push.apply(b,a);return b}return a};try{Array.prototype.slic\
e.call(c.documentElement.childNodes,0)[0].nodeType}catch(t){s=function(a,b){var\
 c=0,d=b||[];if(g.call(a)===\"[object Array]\")Array.prototype.push.apply(d,a);\
else if(typeof a.length==\"number\")for(var e=a.length;c<e;c++)d.push(a[c]);els\
e for(;a[c];c++)d.push(a[c]);return d}}var u,v;c.documentElement.compareDocumen\
tPosition?u=function(a,b){if(a===b){h=!0;return 0}if(!a.compareDocumentPosition\
||!b.compareDocumentPosition)return a.compareDocumentPosition?-1:1;return a.com\
pareDocumentPosition(b)&4?-1:1}:(u=function(a,b){if(a===b){h=!0;return 0}if(a.s\
ourceIndex&&b.sourceIndex)return a.sourceIndex-b.sourceIndex;var c,d,e=[],f=[],\
g=a.parentNode,i=b.parentNode,j=g;if(g===i)return v(a,b);if(!g)return-1;if(!i)r\
eturn 1;while(j)e.unshift(j),j=j.parentNode;j=i;while(j)f.unshift(j),j=j.parent\
Node;c=e.length,d=f.length;for(var k=0;k<c&&k<d;k++)if(e[k]!==f[k])return v(e[k\
],f[k]);return k===c?v(a,f[k],-1):v(e[k],b,1)},v=function(a,b,c){if(a===b)retur\
n c;var d=a.nextSibling;while(d){if(d===b)return-1;d=d.nextSibling}return 1}),f\
unction(){var a=c.createElement(\"div\"),d=\"script\"+(new Date).getTime(),e=c.\
documentElement;a.innerHTML=\"<a name='\"+d+\"'/>\",e.insertBefore(a,e.firstChi\
ld),c.getElementById(d)&&(o.find.ID=function(a,c,d){if(typeof c.getElementById!\
=\"undefined\"&&!d){var e=c.getElementById(a[1]);return e?e.id===a[1]||typeof e\
.getAttributeNode!=\"undefined\"&&e.getAttributeNode(\"id\").nodeValue===a[1]?[\
e]:b:[]}},o.filter.ID=function(a,b){var c=typeof a.getAttributeNode!=\"undefine\
d\"&&a.getAttributeNode(\"id\");return a.nodeType===1&&c&&c.nodeValue===b}),e.r\
emoveChild(a),e=a=null}(),function(){var a=c.createElement(\"div\");a.appendChi\
ld(c.createComment(\"\")),a.getElementsByTagName(\"*\").length>0&&(o.find.TAG=f\
unction(a,b){var c=b.getElementsByTagName(a[1]);if(a[1]===\"*\"){var d=[];for(v\
ar e=0;c[e];e++)c[e].nodeType===1&&d.push(c[e]);c=d}return c}),a.innerHTML=\"<a\
 href='#'></a>\",a.firstChild&&typeof a.firstChild.getAttribute!=\"undefined\"&\
&a.firstChild.getAttribute(\"href\")!==\"#\"&&(o.attrHandle.href=function(a){re\
turn a.getAttribute(\"href\",2)}),a=null}(),c.querySelectorAll&&function(){var \
a=m,b=c.createElement(\"div\"),d=\"__sizzle__\";b.innerHTML=\"<p class='TEST'><\
/p>\";if(!b.querySelectorAll||b.querySelectorAll(\".TEST\").length!==0){m=funct\
ion(b,e,f,g){e=e||c;if(!g&&!m.isXML(e)){var h=/^(\\w+$)|^\\.([\\w\\-]+$)|^#([\
\\w\\-]+$)/.exec(b);if(h&&(e.nodeType===1||e.nodeType===9)){if(h[1])return s(e.\
getElementsByTagName(b),f);if(h[2]&&o.find.CLASS&&e.getElementsByClassName)retu\
rn s(e.getElementsByClassName(h[2]),f)}if(e.nodeType===9){if(b===\"body\"&&e.bo\
dy)return s([e.body],f);if(h&&h[3]){var i=e.getElementById(h[3]);if(!i||!i.pare\
ntNode)return s([],f);if(i.id===h[3])return s([i],f)}try{return s(e.querySelect\
orAll(b),f)}catch(j){}}else if(e.nodeType===1&&e.nodeName.toLowerCase()!==\"obj\
ect\"){var k=e,l=e.getAttribute(\"id\"),n=l||d,p=e.parentNode,q=/^\\s*[+~]/.tes\
t(b);l?n=n.replace(/'/g,\"\\\\$&\"):e.setAttribute(\"id\",n),q&&p&&(e=e.parentN\
ode);try{if(!q||p)return s(e.querySelectorAll(\"[id='\"+n+\"'] \"+b),f)}catch(r\
){}finally{l||k.removeAttribute(\"id\")}}}return a(b,e,f,g)};for(var e in a)m[e\
]=a[e];b=null}}(),function(){var a=c.documentElement,b=a.matchesSelector||a.moz\
MatchesSelector||a.webkitMatchesSelector||a.msMatchesSelector;if(b){var d=!b.ca\
ll(c.createElement(\"div\"),\"div\"),e=!1;try{b.call(c.documentElement,\"[test!\
='']:sizzle\")}catch(f){e=!0}m.matchesSelector=function(a,c){c=c.replace(/\\=\
\\s*([^'\"\\]]*)\\s*\\]/g,\"='$1']\");if(!m.isXML(a))try{if(e||!o.match.PSEUDO.\
test(c)&&!/!=/.test(c)){var f=b.call(a,c);if(f||!d||a.document&&a.document.node\
Type!==11)return f}}catch(g){}return m(c,null,null,[a]).length>0}}}(),function(\
){var a=c.createElement(\"div\");a.innerHTML=\"<div class='test e'></div><div c\
lass='test'></div>\";if(!!a.getElementsByClassName&&a.getElementsByClassName(\"\
e\").length!==0){a.lastChild.className=\"e\";if(a.getElementsByClassName(\"e\")\
.length===1)return;o.order.splice(1,0,\"CLASS\"),o.find.CLASS=function(a,b,c){i\
f(typeof b.getElementsByClassName!=\"undefined\"&&!c)return b.getElementsByClas\
sName(a[1])},a=null}}(),c.documentElement.contains?m.contains=function(a,b){ret\
urn a!==b&&(a.contains?a.contains(b):!0)}:c.documentElement.compareDocumentPosi\
tion?m.contains=function(a,b){return!!(a.compareDocumentPosition(b)&16)}:m.cont\
ains=function(){return!1},m.isXML=function(a){var b=(a?a.ownerDocument||a:0).do\
cumentElement;return b?b.nodeName!==\"HTML\":!1};var y=function(a,b,c){var d,e=\
[],f=\"\",g=b.nodeType?[b]:b;while(d=o.match.PSEUDO.exec(a))f+=d[0],a=a.replace\
(o.match.PSEUDO,\"\");a=o.relative[a]?a+\"*\":a;for(var h=0,i=g.length;h<i;h++)\
m(a,g[h],e,c);return m.filter(f,e)};m.attr=f.attr,m.selectors.attrMap={},f.find\
=m,f.expr=m.selectors,f.expr[\":\"]=f.expr.filters,f.unique=m.uniqueSort,f.text\
=m.getText,f.isXMLDoc=m.isXML,f.contains=m.contains}();var L=/Until$/,M=/^(?:pa\
rents|prevUntil|prevAll)/,N=/,/,O=/^.[^:#\\[\\.,]*$/,P=Array.prototype.slice,Q=\
f.expr.match.globalPOS,R={children:!0,contents:!0,next:!0,prev:!0};f.fn.extend(\
{find:function(a){var b=this,c,d;if(typeof a!=\"string\")return f(a).filter(fun\
ction(){for(c=0,d=b.length;c<d;c++)if(f.contains(b[c],this))return!0});var e=th\
is.pushStack(\"\",\"find\",a),g,h,i;for(c=0,d=this.length;c<d;c++){g=e.length,f\
.find(a,this[c],e);if(c>0)for(h=g;h<e.length;h++)for(i=0;i<g;i++)if(e[i]===e[h]\
){e.splice(h--,1);break}}return e},has:function(a){var b=f(a);return this.filte\
r(function(){for(var a=0,c=b.length;a<c;a++)if(f.contains(this,b[a]))return!0})\
},not:function(a){return this.pushStack(T(this,a,!1),\"not\",a)},filter:functio\
n(a){return this.pushStack(T(this,a,!0),\"filter\",a)},is:function(a){return!!a\
&&(typeof a==\"string\"?Q.test(a)?f(a,this.context).index(this[0])>=0:f.filter(\
a,this).length>0:this.filter(a).length>0)},closest:function(a,b){var c=[],d,e,g\
=this[0];if(f.isArray(a)){var h=1;while(g&&g.ownerDocument&&g!==b){for(d=0;d<a.\
length;d++)f(g).is(a[d])&&c.push({selector:a[d],elem:g,level:h});g=g.parentNode\
,h++}return c}var i=Q.test(a)||typeof a!=\"string\"?f(a,b||this.context):0;for(\
d=0,e=this.length;d<e;d++){g=this[d];while(g){if(i?i.index(g)>-1:f.find.matches\
Selector(g,a)){c.push(g);break}g=g.parentNode;if(!g||!g.ownerDocument||g===b||g\
.nodeType===11)break}}c=c.length>1?f.unique(c):c;return this.pushStack(c,\"clos\
est\",a)},index:function(a){if(!a)return this[0]&&this[0].parentNode?this.prevA\
ll().length:-1;if(typeof a==\"string\")return f.inArray(this[0],f(a));return f.\
inArray(a.jquery?a[0]:a,this)},add:function(a,b){var c=typeof a==\"string\"?f(a\
,b):f.makeArray(a&&a.nodeType?[a]:a),d=f.merge(this.get(),c);return this.pushSt\
ack(S(c[0])||S(d[0])?d:f.unique(d))},andSelf:function(){return this.add(this.pr\
evObject)}}),f.each({parent:function(a){var b=a.parentNode;return b&&b.nodeType\
!==11?b:null},parents:function(a){return f.dir(a,\"parentNode\")},parentsUntil:\
function(a,b,c){return f.dir(a,\"parentNode\",c)},next:function(a){return f.nth\
(a,2,\"nextSibling\")},prev:function(a){return f.nth(a,2,\"previousSibling\")},\
nextAll:function(a){return f.dir(a,\"nextSibling\")},prevAll:function(a){return\
 f.dir(a,\"previousSibling\")},nextUntil:function(a,b,c){return f.dir(a,\"nextS\
ibling\",c)},prevUntil:function(a,b,c){return f.dir(a,\"previousSibling\",c)},s\
iblings:function(a){return f.sibling((a.parentNode||{}).firstChild,a)},children\
:function(a){return f.sibling(a.firstChild)},contents:function(a){return f.node\
Name(a,\"iframe\")?a.contentDocument||a.contentWindow.document:f.makeArray(a.ch\
ildNodes)}},function(a,b){f.fn[a]=function(c,d){var e=f.map(this,b,c);L.test(a)\
||(d=c),d&&typeof d==\"string\"&&(e=f.filter(d,e)),e=this.length>1&&!R[a]?f.uni\
que(e):e,(this.length>1||N.test(d))&&M.test(a)&&(e=e.reverse());return this.pus\
hStack(e,a,P.call(arguments).join(\",\"))}}),f.extend({filter:function(a,b,c){c\
&&(a=\":not(\"+a+\")\");return b.length===1?f.find.matchesSelector(b[0],a)?[b[0\
]]:[]:f.find.matches(a,b)},dir:function(a,c,d){var e=[],g=a[c];while(g&&g.nodeT\
ype!==9&&(d===b||g.nodeType!==1||!f(g).is(d)))g.nodeType===1&&e.push(g),g=g[c];\
return e},nth:function(a,b,c,d){b=b||1;var e=0;for(;a;a=a[c])if(a.nodeType===1&\
&++e===b)break;return a},sibling:function(a,b){var c=[];for(;a;a=a.nextSibling)\
a.nodeType===1&&a!==b&&c.push(a);return c}});var V=\"abbr|article|aside|audio|b\
di|canvas|data|datalist|details|figcaption|figure|footer|header|hgroup|mark|met\
er|nav|output|progress|section|summary|time|video\",W=/ jQuery\\d+=\"(?:\\d+|nu\
ll)\"/g,X=/^\\s+/,Y=/<(?!area|br|col|embed|hr|img|input|link|meta|param)(([\\w:\
]+)[^>]*)\\/>/ig,Z=/<([\\w:]+)/,$=/<tbody/i,_=/<|&#?\\w+;/,ba=/<(?:script|style\
)/i,bb=/<(?:script|object|embed|option|style)/i,bc=new RegExp(\"<(?:\"+V+\")[\
\\\\s/>]\",\"i\"),bd=/checked\\s*(?:[^=]|=\\s*.checked.)/i,be=/\\/(java|ecma)sc\
ript/i,bf=/^\\s*<!(?:\\[CDATA\\[|\\-\\-)/,bg={option:[1,\"<select multiple='mul\
tiple'>\",\"</select>\"],legend:[1,\"<fieldset>\",\"</fieldset>\"],thead:[1,\"<\
table>\",\"</table>\"],tr:[2,\"<table><tbody>\",\"</tbody></table>\"],td:[3,\"<\
table><tbody><tr>\",\"</tr></tbody></table>\"],col:[2,\"<table><tbody></tbody><\
colgroup>\",\"</colgroup></table>\"],area:[1,\"<map>\",\"</map>\"],_default:[0,\
\"\",\"\"]},bh=U(c);bg.optgroup=bg.option,bg.tbody=bg.tfoot=bg.colgroup=bg.capt\
ion=bg.thead,bg.th=bg.td,f.support.htmlSerialize||(bg._default=[1,\"div<div>\",\
\"</div>\"]),f.fn.extend({text:function(a){return f.access(this,function(a){ret\
urn a===b?f.text(this):this.empty().append((this[0]&&this[0].ownerDocument||c).\
createTextNode(a))},null,a,arguments.length)},wrapAll:function(a){if(f.isFuncti\
on(a))return this.each(function(b){f(this).wrapAll(a.call(this,b))});if(this[0]\
){var b=f(a,this[0].ownerDocument).eq(0).clone(!0);this[0].parentNode&&b.insert\
Before(this[0]),b.map(function(){var a=this;while(a.firstChild&&a.firstChild.no\
deType===1)a=a.firstChild;return a}).append(this)}return this},wrapInner:functi\
on(a){if(f.isFunction(a))return this.each(function(b){f(this).wrapInner(a.call(\
this,b))});return this.each(function(){var b=f(this),c=b.contents();c.length?c.\
wrapAll(a):b.append(a)})},wrap:function(a){var b=f.isFunction(a);return this.ea\
ch(function(c){f(this).wrapAll(b?a.call(this,c):a)})},unwrap:function(){return \
this.parent().each(function(){f.nodeName(this,\"body\")||f(this).replaceWith(th\
is.childNodes)}).end()},append:function(){return this.domManip(arguments,!0,fun\
ction(a){this.nodeType===1&&this.appendChild(a)})},prepend:function(){return th\
is.domManip(arguments,!0,function(a){this.nodeType===1&&this.insertBefore(a,thi\
s.firstChild)})},before:function(){if(this[0]&&this[0].parentNode)return this.d\
omManip(arguments,!1,function(a){this.parentNode.insertBefore(a,this)});if(argu\
ments.length){var a=f .clean(arguments);a.push.apply(a,this.toArray());return t\
his.pushStack(a,\"before\",arguments)}},after:function(){if(this[0]&&this[0].pa\
rentNode)return this.domManip(arguments,!1,function(a){this.parentNode.insertBe\
fore(a,this.nextSibling)});if(arguments.length){var a=this.pushStack(this,\"aft\
er\",arguments);a.push.apply(a,f.clean(arguments));return a}},remove:function(a\
,b){for(var c=0,d;(d=this[c])!=null;c++)if(!a||f.filter(a,[d]).length)!b&&d.nod\
eType===1&&(f.cleanData(d.getElementsByTagName(\"*\")),f.cleanData([d])),d.pare\
ntNode&&d.parentNode.removeChild(d);return this},empty:function(){for(var a=0,b\
;(b=this[a])!=null;a++){b.nodeType===1&&f.cleanData(b.getElementsByTagName(\"*\
\"));while(b.firstChild)b.removeChild(b.firstChild)}return this},clone:function\
(a,b){a=a==null?!1:a,b=b==null?a:b;return this.map(function(){return f.clone(th\
is,a,b)})},html:function(a){return f.access(this,function(a){var c=this[0]||{},\
d=0,e=this.length;if(a===b)return c.nodeType===1?c.innerHTML.replace(W,\"\"):nu\
ll;if(typeof a==\"string\"&&!ba.test(a)&&(f.support.leadingWhitespace||!X.test(\
a))&&!bg[(Z.exec(a)||[\"\",\"\"])[1].toLowerCase()]){a=a.replace(Y,\"<$1></$2>\
\");try{for(;d<e;d++)c=this[d]||{},c.nodeType===1&&(f.cleanData(c.getElementsBy\
TagName(\"*\")),c.innerHTML=a);c=0}catch(g){}}c&&this.empty().append(a)},null,a\
,arguments.length)},replaceWith:function(a){if(this[0]&&this[0].parentNode){if(\
f.isFunction(a))return this.each(function(b){var c=f(this),d=c.html();c.replace\
With(a.call(this,b,d))});typeof a!=\"string\"&&(a=f(a).detach());return this.ea\
ch(function(){var b=this.nextSibling,c=this.parentNode;f(this).remove(),b?f(b).\
before(a):f(c).append(a)})}return this.length?this.pushStack(f(f.isFunction(a)?\
a():a),\"replaceWith\",a):this},detach:function(a){return this.remove(a,!0)},do\
mManip:function(a,c,d){var e,g,h,i,j=a[0],k=[];if(!f.support.checkClone&&argume\
nts.length===3&&typeof j==\"string\"&&bd.test(j))return this.each(function(){f(\
this).domManip(a,c,d,!0)});if(f.isFunction(j))return this.each(function(e){var \
g=f(this);a[0]=j.call(this,e,c?g.html():b),g.domManip(a,c,d)});if(this[0]){i=j&\
&j.parentNode,f.support.parentNode&&i&&i.nodeType===11&&i.childNodes.length===t\
his.length?e={fragment:i}:e=f.buildFragment(a,this,k),h=e.fragment,h.childNodes\
.length===1?g=h=h.firstChild:g=h.firstChild;if(g){c=c&&f.nodeName(g,\"tr\");for\
(var l=0,m=this.length,n=m-1;l<m;l++)d.call(c?bi(this[l],g):this[l],e.cacheable\
||m>1&&l<n?f.clone(h,!0,!0):h)}k.length&&f.each(k,function(a,b){b.src?f.ajax({t\
ype:\"GET\",global:!1,url:b.src,async:!1,dataType:\"script\"}):f.globalEval((b.\
text||b.textContent||b.innerHTML||\"\").replace(bf,\"/*$0*/\")),b.parentNode&&b\
.parentNode.removeChild(b)})}return this}}),f.buildFragment=function(a,b,d){var\
 e,g,h,i,j=a[0];b&&b[0]&&(i=b[0].ownerDocument||b[0]),i.createDocumentFragment|\
|(i=c),a.length===1&&typeof j==\"string\"&&j.length<512&&i===c&&j.charAt(0)===\
\"<\"&&!bb.test(j)&&(f.support.checkClone||!bd.test(j))&&(f.support.html5Clone|\
|!bc.test(j))&&(g=!0,h=f.fragments[j],h&&h!==1&&(e=h)),e||(e=i.createDocumentFr\
agment(),f.clean(a,i,e,d)),g&&(f.fragments[j]=h?e:1);return{fragment:e,cacheabl\
e:g}},f.fragments={},f.each({appendTo:\"append\",prependTo:\"prepend\",insertBe\
fore:\"before\",insertAfter:\"after\",replaceAll:\"replaceWith\"},function(a,b)\
{f.fn[a]=function(c){var d=[],e=f(c),g=this.length===1&&this[0].parentNode;if(g\
&&g.nodeType===11&&g.childNodes.length===1&&e.length===1){e[b](this[0]);return \
this}for(var h=0,i=e.length;h<i;h++){var j=(h>0?this.clone(!0):this).get();f(e[\
h])[b](j),d=d.concat(j)}return this.pushStack(d,a,e.selector)}}),f.extend({clon\
e:function(a,b,c){var d,e,g,h=f.support.html5Clone||f.isXMLDoc(a)||!bc.test(\"<\
\"+a.nodeName+\">\")?a.cloneNode(!0):bo(a);if((!f.support.noCloneEvent||!f.supp\
ort.noCloneChecked)&&(a.nodeType===1||a.nodeType===11)&&!f.isXMLDoc(a)){bk(a,h)\
,d=bl(a),e=bl(h);for(g=0;d[g];++g)e[g]&&bk(d[g],e[g])}if(b){bj(a,h);if(c){d=bl(\
a),e=bl(h);for(g=0;d[g];++g)bj(d[g],e[g])}}d=e=null;return h},clean:function(a,\
b,d,e){var g,h,i,j=[];b=b||c,typeof b.createElement==\"undefined\"&&(b=b.ownerD\
ocument||b[0]&&b[0].ownerDocument||c);for(var k=0,l;(l=a[k])!=null;k++){typeof \
l==\"number\"&&(l+=\"\");if(!l)continue;if(typeof l==\"string\")if(!_.test(l))l\
=b.createTextNode(l);else{l=l.replace(Y,\"<$1></$2>\");var m=(Z.exec(l)||[\"\",\
\"\"])[1].toLowerCase(),n=bg[m]||bg._default,o=n[0],p=b.createElement(\"div\"),\
q=bh.childNodes,r;b===c?bh.appendChild(p):U(b).appendChild(p),p.innerHTML=n[1]+\
l+n[2];while(o--)p=p.lastChild;if(!f.support.tbody){var s=$.test(l),t=m===\"tab\
le\"&&!s?p.firstChild&&p.firstChild.childNodes:n[1]===\"<table>\"&&!s?p.childNo\
des:[];for(i=t.length-1;i>=0;--i)f.nodeName(t[i],\"tbody\")&&!t[i].childNodes.l\
ength&&t[i].parentNode.removeChild(t[i])}!f.support.leadingWhitespace&&X.test(l\
)&&p.insertBefore(b.createTextNode(X.exec(l)[0]),p.firstChild),l=p.childNodes,p\
&&(p.parentNode.removeChild(p),q.length>0&&(r=q[q.length-1],r&&r.parentNode&&r.\
parentNode.removeChild(r)))}var u;if(!f.support.appendChecked)if(l[0]&&typeof (\
u=l.length)==\"number\")for(i=0;i<u;i++)bn(l[i]);else bn(l);l.nodeType?j.push(l\
):j=f.merge(j,l)}if(d){g=function(a){return!a.type||be.test(a.type)};for(k=0;j[\
k];k++){h=j[k];if(e&&f.nodeName(h,\"script\")&&(!h.type||be.test(h.type)))e.pus\
h(h.parentNode?h.parentNode.removeChild(h):h);else{if(h.nodeType===1){var v=f.g\
rep(h.getElementsByTagName(\"script\"),g);j.splice.apply(j,[k+1,0].concat(v))}d\
.appendChild(h)}}}return j},cleanData:function(a){var b,c,d=f.cache,e=f.event.s\
pecial,g=f.support.deleteExpando;for(var h=0,i;(i=a[h])!=null;h++){if(i.nodeNam\
e&&f.noData[i.nodeName.toLowerCase()])continue;c=i[f.expando];if(c){b=d[c];if(b\
&&b.events){for(var j in b.events)e[j]?f.event.remove(i,j):f.removeEvent(i,j,b.\
handle);b.handle&&(b.handle.elem=null)}g?delete i[f.expando]:i.removeAttribute&\
&i.removeAttribute(f.expando),delete d[c]}}}});var bp=/alpha\\([^)]*\\)/i,bq=/o\
pacity=([^)]*)/,br=/([A-Z]|^ms)/g,bs=/^[\\-+]?(?:\\d*\\.)?\\d+$/i,bt=/^-?(?:\\d\
*\\.)?\\d+(?!px)[^\\d\\s]+$/i,bu=/^([\\-+])=([\\-+.\\de]+)/,bv=/^margin/,bw={po\
sition:\"absolute\",visibility:\"hidden\",display:\"block\"},bx=[\"Top\",\"Righ\
t\",\"Bottom\",\"Left\"],by,bz,bA;f.fn.css=function(a,c){return f.access(this,f\
unction(a,c,d){return d!==b?f.style(a,c,d):f.css(a,c)},a,c,arguments.length>1)}\
,f.extend({cssHooks:{opacity:{get:function(a,b){if(b){var c=by(a,\"opacity\");r\
eturn c===\"\"?\"1\":c}return a.style.opacity}}},cssNumber:{fillOpacity:!0,font\
Weight:!0,lineHeight:!0,opacity:!0,orphans:!0,widows:!0,zIndex:!0,zoom:!0},cssP\
rops:{\"float\":f.support.cssFloat?\"cssFloat\":\"styleFloat\"},style:function(\
a,c,d,e){if(!!a&&a.nodeType!==3&&a.nodeType!==8&&!!a.style){var g,h,i=f.camelCa\
se(c),j=a.style,k=f.cssHooks[i];c=f.cssProps[i]||i;if(d===b){if(k&&\"get\"in k&\
&(g=k.get(a,!1,e))!==b)return g;return j[c]}h=typeof d,h===\"string\"&&(g=bu.ex\
ec(d))&&(d=+(g[1]+1)*+g[2]+parseFloat(f.css(a,c)),h=\"number\");if(d==null||h==\
=\"number\"&&isNaN(d))return;h===\"number\"&&!f.cssNumber[i]&&(d+=\"px\");if(!k\
||!(\"set\"in k)||(d=k.set(a,d))!==b)try{j[c]=d}catch(l){}}},css:function(a,c,d\
){var e,g;c=f.camelCase(c),g=f.cssHooks[c],c=f.cssProps[c]||c,c===\"cssFloat\"&\
&(c=\"float\");if(g&&\"get\"in g&&(e=g.get(a,!0,d))!==b)return e;if(by)return b\
y(a,c)},swap:function(a,b,c){var d={},e,f;for(f in b)d[f]=a.style[f],a.style[f]\
=b[f];e=c.call(a);for(f in b)a.style[f]=d[f];return e}}),f.curCSS=f.css,c.defau\
ltView&&c.defaultView.getComputedStyle&&(bz=function(a,b){var c,d,e,g,h=a.style\
;b=b.replace(br,\"-$1\").toLowerCase(),(d=a.ownerDocument.defaultView)&&(e=d.ge\
tComputedStyle(a,null))&&(c=e.getPropertyValue(b),c===\"\"&&!f.contains(a.owner\
Document.documentElement,a)&&(c=f.style(a,b))),!f.support.pixelMargin&&e&&bv.te\
st(b)&&bt.test(c)&&(g=h.width,h.width=c,c=e.width,h.width=g);return c}),c.docum\
entElement.currentStyle&&(bA=function(a,b){var c,d,e,f=a.currentStyle&&a.curren\
tStyle[b],g=a.style;f==null&&g&&(e=g[b])&&(f=e),bt.test(f)&&(c=g.left,d=a.runti\
meStyle&&a.runtimeStyle.left,d&&(a.runtimeStyle.left=a.currentStyle.left),g.lef\
t=b===\"fontSize\"?\"1em\":f,f=g.pixelLeft+\"px\",g.left=c,d&&(a.runtimeStyle.l\
eft=d));return f===\"\"?\"auto\":f}),by=bz||bA,f.each([\"height\",\"width\"],fu\
nction(a,b){f.cssHooks[b]={get:function(a,c,d){if(c)return a.offsetWidth!==0?bB\
(a,b,d):f.swap(a,bw,function(){return bB(a,b,d)})},set:function(a,b){return bs.\
test(b)?b+\"px\":b}}}),f.support.opacity||(f.cssHooks.opacity={get:function(a,b\
){return bq.test((b&&a.currentStyle?a.currentStyle.filter:a.style.filter)||\"\"\
)?parseFloat(RegExp.$1)/100+\"\":b?\"1\":\"\"},set:function(a,b){var c=a.style,\
d=a.currentStyle,e=f.isNumeric(b)?\"alpha(opacity=\"+b*100+\")\":\"\",g=d&&d.fi\
lter||c.filter||\"\";c.zoom=1;if(b>=1&&f.trim(g.replace(bp,\"\"))===\"\"){c.rem\
oveAttribute(\"filter\");if(d&&!d.filter)return}c.filter=bp.test(g)?g.replace(b\
p,e):g+\" \"+e}}),f(function(){f.support.reliableMarginRight||(f.cssHooks.margi\
nRight={get:function(a,b){return f.swap(a,{display:\"inline-block\"},function()\
{return b?by(a,\"margin-right\"):a.style.marginRight})}})}),f.expr&&f.expr.filt\
ers&&(f.expr.filters.hidden=function(a){var b=a.offsetWidth,c=a.offsetHeight;re\
turn b===0&&c===0||!f.support.reliableHiddenOffsets&&(a.style&&a.style.display|\
|f.css(a,\"display\"))===\"none\"},f.expr.filters.visible=function(a){return!f.\
expr.filters.hidden(a)}),f.each({margin:\"\",padding:\"\",border:\"Width\"},fun\
ction(a,b){f.cssHooks[a+b]={expand:function(c){var d,e=typeof c==\"string\"?c.s\
plit(\" \"):[c],f={};for(d=0;d<4;d++)f[a+bx[d]+b]=e[d]||e[d-2]||e[0];return f}}\
});var bC=/%20/g,bD=/\\[\\]$/,bE=/\\r?\\n/g,bF=/#.*$/,bG=/^(.*?):[ \\t]*([^\\r\
\\n]*)\\r?$/mg,bH=/^(?:color|date|datetime|datetime-local|email|hidden|month|nu\
mber|password|range|search|tel|text|time|url|week)$/i,bI=/^(?:about|app|app\\-s\
torage|.+\\-extension|file|res|widget):$/,bJ=/^(?:GET|HEAD)$/,bK=/^\\/\\//,bL=/\
\\?/,bM=/<script\\b[^<]*(?:(?!<\\/script>)<[^<]*)*<\\/script>/gi,bN=/^(?:select\
|textarea)/i,bO=/\\s+/,bP=/([?&])_=[^&]*/,bQ=/^([\\w\\+\\.\\-]+:)(?:\\/\\/([^\
\\/?#:]*)(?::(\\d+))?)?/,bR=f.fn.load,bS={},bT={},bU,bV,bW=[\"*/\"]+[\"*\"];try\
{bU=e.href}catch(bX){bU=c.createElement(\"a\"),bU.href=\"\",bU=bU.href}bV=bQ.ex\
ec(bU.toLowerCase())||[],f.fn.extend({load:function(a,c,d){if(typeof a!=\"strin\
g\"&&bR)return bR.apply(this,arguments);if(!this.length)return this;var e=a.ind\
exOf(\" \");if(e>=0){var g=a.slice(e,a.length);a=a.slice(0,e)}var h=\"GET\";c&&\
(f.isFunction(c)?(d=c,c=b):typeof c==\"object\"&&(c=f.param(c,f.ajaxSettings.tr\
aditional),h=\"POST\"));var i=this;f.ajax({url:a,type:h,dataType:\"html\",data:\
c,complete:function(a,b,c){c=a.responseText,a.isResolved()&&(a.done(function(a)\
{c=a}),i.html(g?f(\"<div>\").append(c.replace(bM,\"\")).find(g):c)),d&&i.each(d\
,[c,b,a])}});return this},serialize:function(){return f.param(this.serializeArr\
ay())},serializeArray:function(){return this.map(function(){return this.element\
s?f.makeArray(this.elements):this}).filter(function(){return this.name&&!this.d\
isabled&&(this.checked||bN.test(this.nodeName)||bH.test(this.type))}).map(funct\
ion(a,b){var c=f(this).val();return c==null?null:f.isArray(c)?f.map(c,function(\
a,c){return{name:b.name,value:a.replace(bE,\"\\r\\n\")}}):{name:b.name,value:c.\
replace(bE,\"\\r\\n\")}}).get()}}),f.each(\"ajaxStart ajaxStop ajaxComplete aja\
xError ajaxSuccess ajaxSend\".split(\" \"),function(a,b){f.fn[b]=function(a){re\
turn this.on(b,a)}}),f.each([\"get\",\"post\"],function(a,c){f[c]=function(a,d,\
e,g){f.isFunction(d)&&(g=g||e,e=d,d=b);return f.ajax({type:c,url:a,data:d,succe\
ss:e,dataType:g})}}),f.extend({getScript:function(a,c){return f.get(a,b,c,\"scr\
ipt\")},getJSON:function(a,b,c){return f.get(a,b,c,\"json\")},ajaxSetup:functio\
n(a,b){b?b$(a,f.ajaxSettings):(b=a,a=f.ajaxSettings),b$(a,b);return a},ajaxSett\
ings:{url:bU,isLocal:bI.test(bV[1]),global:!0,type:\"GET\",contentType:\"applic\
ation/x-www-form-urlencoded;charset=UTF-8\",processData:!0,async:!0,accepts:{xm\
l:\"application/xml, text/xml\",html:\"text/html\",text:\"text/plain\",json:\"a\
pplication/json, text/javascript\",\"*\":bW},contents:{xml:/xml/,html:/html/,js\
on:/json/},responseFields:{xml:\"responseXML\",text:\"responseText\"},converter\
s:{\"* text\":a.String,\"text html\":!0,\"text json\":f.parseJSON,\"text xml\":\
f.parseXML},flatOptions:{context:!0,url:!0}},ajaxPrefilter:bY(bS),ajaxTransport\
:bY(bT),ajax:function(a,c){function w(a,c,l,m){if(s!==2){s=2,q&&clearTimeout(q)\
,p=b,n=m||\"\",v.readyState=a>0?4:0;var o,r,u,w=c,x=l?ca(d,v,l):b,y,z;if(a>=200\
&&a<300||a===304){if(d.ifModified){if(y=v.getResponseHeader(\"Last-Modified\"))\
f.lastModified[k]=y;if(z=v.getResponseHeader(\"Etag\"))f.etag[k]=z}if(a===304)w\
=\"notmodified\",o=!0;else try{r=cb(d,x),w=\"success\",o=!0}catch(A){w=\"parser\
error\",u=A}}else{u=w;if(!w||a)w=\"error\",a<0&&(a=0)}v.status=a,v.statusText=\
\"\"+(c||w),o?h.resolveWith(e,[r,w,v]):h.rejectWith(e,[v,w,u]),v.statusCode(j),\
j=b,t&&g.trigger(\"ajax\"+(o?\"Success\":\"Error\"),[v,d,o?r:u]),i.fireWith(e,[\
v,w]),t&&(g.trigger(\"ajaxComplete\",[v,d]),--f.active||f.event.trigger(\"ajaxS\
top\"))}}typeof a==\"object\"&&(c=a,a=b),c=c||{};var d=f.ajaxSetup({},c),e=d.co\
ntext||d,g=e!==d&&(e.nodeType||e instanceof f)?f(e):f.event,h=f.Deferred(),i=f.\
Callbacks(\"once memory\"),j=d.statusCode||{},k,l={},m={},n,o,p,q,r,s=0,t,u,v={\
readyState:0,setRequestHeader:function(a,b){if(!s){var c=a.toLowerCase();a=m[c]\
=m[c]||a,l[a]=b}return this},getAllResponseHeaders:function(){return s===2?n:nu\
ll},getResponseHeader:function(a){var c;if(s===2){if(!o){o={};while(c=bG.exec(n\
))o[c[1].toLowerCase()]=c[2]}c=o[a.toLowerCase()]}return c===b?null:c},override\
MimeType:function(a){s||(d.mimeType=a);return this},abort:function(a){a=a||\"ab\
ort\",p&&p.abort(a),w(0,a);return this}};h.promise(v),v.success=v.done,v.error=\
v.fail,v.complete=i.add,v.statusCode=function(a){if(a){var b;if(s<2)for(b in a)\
j[b]=[j[b],a[b]];else b=a[v.status],v.then(b,b)}return this},d.url=((a||d.url)+\
\"\").replace(bF,\"\").replace(bK,bV[1]+\"//\"),d.dataTypes=f.trim(d.dataType||\
\"*\").toLowerCase().split(bO),d.crossDomain==null&&(r=bQ.exec(d.url.toLowerCas\
e()),d.crossDomain=!(!r||r[1]==bV[1]&&r[2]==bV[2]&&(r[3]||(r[1]===\"http:\"?80:\
443))==(bV[3]||(bV[1]===\"http:\"?80:443)))),d.data&&d.processData&&typeof d.da\
ta!=\"string\"&&(d.data=f.param(d.data,d.traditional)),bZ(bS,d,c,v);if(s===2)re\
turn!1;t=d.global,d.type=d.type.toUpperCase(),d.hasContent=!bJ.test(d.type),t&&\
f.active++===0&&f.event.trigger(\"ajaxStart\");if(!d.hasContent){d.data&&(d.url\
+=(bL.test(d.url)?\"&\":\"?\")+d.data,delete d.data),k=d.url;if(d.cache===!1){v\
ar x=f.now(),y=d.url.replace(bP,\"$1_=\"+x);d.url=y+(y===d.url?(bL.test(d.url)?\
\"&\":\"?\")+\"_=\"+x:\"\")}}(d.data&&d.hasContent&&d.contentType!==!1||c.conte\
ntType)&&v.setRequestHeader(\"Content-Type\",d.contentType),d.ifModified&&(k=k|\
|d.url,f.lastModified[k]&&v.setRequestHeader(\"If-Modified-Since\",f.lastModifi\
ed[k]),f.etag[k]&&v.setRequestHeader(\"If-None-Match\",f.etag[k])),v.setRequest\
Header(\"Accept\",d.dataTypes[0]&&d.accepts[d.dataTypes[0]]?d.accepts[d.dataTyp\
es[0]]+(d.dataTypes[0]!==\"*\"?\", \"+bW+\";q=0.01\":\"\"):d.accepts[\"*\"]);fo\
r(u in d.headers)v.setRequestHeader(u,d.headers[u]);if(d.beforeSend&&(d.beforeS\
end.call(e,v,d)===!1||s===2)){v.abort();return!1}for(u in{success:1,error:1,com\
plete:1})v[u](d[u]);p=bZ(bT,d,c,v);if(!p)w(-1,\"No Transport\");else{v.readySta\
te=1,t&&g.trigger(\"ajaxSend\",[v,d]),d.async&&d.timeout>0&&(q=setTimeout(funct\
ion(){v.abort(\"timeout\")},d.timeout));try{s=1,p.send(l,w)}catch(z){if(s<2)w(-\
1,z);else throw z}}return v},param:function(a,c){var d=[],e=function(a,b){b=f.i\
sFunction(b)?b():b,d[d.length]=encodeURIComponent(a)+\"=\"+encodeURIComponent(b\
)};c===b&&(c=f.ajaxSettings.traditional);if(f.isArray(a)||a.jquery&&!f.isPlainO\
bject(a))f.each(a,function(){e(this.name,this.value)});else for(var g in a)b_(g\
,a[g],c,e);return d.join(\"&\").replace(bC,\"+\")}}),f.extend({active:0,lastMod\
ified:{},etag:{}});var cc=f.now(),cd=/(\\=)\\?(&|$)|\\?\\?/i;f.ajaxSetup({jsonp\
:\"callback\",jsonpCallback:function(){return f.expando+\"_\"+cc++}}),f.ajaxPre\
filter(\"json jsonp\",function(b,c,d){var e=typeof b.data==\"string\"&&/^applic\
ation\\/x\\-www\\-form\\-urlencoded/.test(b.contentType);if(b.dataTypes[0]===\"\
jsonp\"||b.jsonp!==!1&&(cd.test(b.url)||e&&cd.test(b.data))){var g,h=b.jsonpCal\
lback=f.isFunction(b.jsonpCallback)?b.jsonpCallback():b.jsonpCallback,i=a[h],j=\
b.url,k=b.data,l=\"$1\"+h+\"$2\";b.jsonp!==!1&&(j=j.replace(cd,l),b.url===j&&(e\
&&(k=k.replace(cd,l)),b.data===k&&(j+=(/\\?/.test(j)?\"&\":\"?\")+b.jsonp+\"=\"\
+h))),b.url=j,b.data=k,a[h]=function(a){g=[a]},d.always(function(){a[h]=i,g&&f.\
isFunction(i)&&a[h](g[0])}),b.converters[\"script json\"]=function(){g||f.error\
(h+\" was not called\");return g[0]},b.dataTypes[0]=\"json\";return\"script\"}}\
),f.ajaxSetup({accepts:{script:\"text/javascript, application/javascript, appli\
cation/ecmascript, application/x-ecmascript\"},contents:{script:/javascript|ecm\
ascript/},converters:{\"text script\":function(a){f.globalEval(a);return a}}}),\
f.ajaxPrefilter(\"script\",function(a){a.cache===b&&(a.cache=!1),a.crossDomain&\
&(a.type=\"GET\",a.global=!1)}),f.ajaxTransport(\"script\",function(a){if(a.cro\
ssDomain){var d,e=c.head||c.getElementsByTagName(\"head\")[0]||c.documentElemen\
t;return{send:function(f,g){d=c.createElement(\"script\"),d.async=\"async\",a.s\
criptCharset&&(d.charset=a.scriptCharset),d.src=a.url,d.onload=d.onreadystatech\
ange=function(a,c){if(c||!d.readyState||/loaded|complete/.test(d.readyState))d.\
onload=d.onreadystatechange=null,e&&d.parentNode&&e.removeChild(d),d=b,c||g(200\
,\"success\")},e.insertBefore(d,e.firstChild)},abort:function(){d&&d.onload(0,1\
)}}}});var ce=a.ActiveXObject?function(){for(var a in cg)cg[a](0,1)}:!1,cf=0,cg\
;f.ajaxSettings.xhr=a.ActiveXObject?function(){return!this.isLocal&&ch()||ci()}\
:ch,function(a){f.extend(f.support,{ajax:!!a,cors:!!a&&\"withCredentials\"in a}\
)}(f.ajaxSettings.xhr()),f.support.ajax&&f.ajaxTransport(function(c){if(!c.cros\
sDomain||f.support.cors){var d;return{send:function(e,g){var h=c.xhr(),i,j;c.us\
ername?h.open(c.type,c.url,c.async,c.username,c.password):h.open(c.type,c.url,c\
.async);if(c.xhrFields)for(j in c.xhrFields)h[j]=c.xhrFields[j];c.mimeType&&h.o\
verrideMimeType&&h.overrideMimeType(c.mimeType),!c.crossDomain&&!e[\"X-Requeste\
d-With\"]&&(e[\"X-Requested-With\"]=\"XMLHttpRequest\");try{for(j in e)h.setReq\
uestHeader(j,e[j])}catch(k){}h.send(c.hasContent&&c.data||null),d=function(a,e)\
{var j,k,l,m,n;try{if(d&&(e||h.readyState===4)){d=b,i&&(h.onreadystatechange=f.\
noop,ce&&delete cg[i]);if(e)h.readyState!==4&&h.abort();else{j=h.status,l=h.get\
AllResponseHeaders(),m={},n=h.responseXML,n&&n.documentElement&&(m.xml=n);try{m\
.text=h.responseText}catch(a){}try{k=h.statusText}catch(o){k=\"\"}!j&&c.isLocal\
&&!c.crossDomain?j=m.text?200:404:j===1223&&(j=204)}}}catch(p){e||g(-1,p)}m&&g(\
j,k,m,l)},!c.async||h.readyState===4?d():(i=++cf,ce&&(cg||(cg={},f(a).unload(ce\
)),cg[i]=d),h.onreadystatechange=d)},abort:function(){d&&d(0,1)}}}});var cj={},\
ck,cl,cm=/^(?:toggle|show|hide)$/,cn=/^([+\\-]=)?([\\d+.\\-]+)([a-z%]*)$/i,co,c\
p=[[\"height\",\"marginTop\",\"marginBottom\",\"paddingTop\",\"paddingBottom\"]\
,[\"width\",\"marginLeft\",\"marginRight\",\"paddingLeft\",\"paddingRight\"],[\
\"opacity\"]],cq;f.fn.extend({show:function(a,b,c){var d,e;if(a||a===0)return t\
his.animate(ct(\"show\",3),a,b,c);for(var g=0,h=this.length;g<h;g++)d=this[g],d\
.style&&(e=d.style.display,!f._data(d,\"olddisplay\")&&e===\"none\"&&(e=d.style\
.display=\"\"),(e===\"\"&&f.css(d,\"display\")===\"none\"||!f.contains(d.ownerD\
ocument.documentElement,d))&&f._data(d,\"olddisplay\",cu(d.nodeName)));for(g=0;\
g<h;g++){d=this[g];if(d.style){e=d.style.display;if(e===\"\"||e===\"none\")d.st\
yle.display=f._data(d,\"olddisplay\")||\"\"}}return this},hide:function(a,b,c){\
if(a||a===0)return this.animate(ct(\"hide\",3),a,b,c);var d,e,g=0,h=this.length\
;for(;g<h;g++)d=this[g],d.style&&(e=f.css(d,\"display\"),e!==\"none\"&&!f._data\
(d,\"olddisplay\")&&f._data(d,\"olddisplay\",e));for(g=0;g<h;g++)this[g].style&\
&(this[g].style.display=\"none\");return this},_toggle:f.fn.toggle,toggle:funct\
ion(a,b,c){var d=typeof a==\"boolean\";f.isFunction(a)&&f.isFunction(b)?this._t\
oggle.apply(this,arguments):a==null||d?this.each(function(){var b=d?a:f(this).i\
s(\":hidden\");f(this)[b?\"show\":\"hide\"]()}):this.animate(ct(\"toggle\",3),a\
,b,c);return this},fadeTo:function(a,b,c,d){return this.filter(\":hidden\").css\
(\"opacity\",0).show().end().animate({opacity:b},a,c,d)},animate:function(a,b,c\
,d){function g(){e.queue===!1&&f._mark(this);var b=f.extend({},e),c=this.nodeTy\
pe===1,d=c&&f(this).is(\":hidden\"),g,h,i,j,k,l,m,n,o,p,q;b.animatedProperties=\
{};for(i in a){g=f.camelCase(i),i!==g&&(a[g]=a[i],delete a[i]);if((k=f.cssHooks\
[g])&&\"expand\"in k){l=k.expand(a[g]),delete a[g];for(i in l)i in a||(a[i]=l[i\
])}}for(g in a){h=a[g],f.isArray(h)?(b.animatedProperties[g]=h[1],h=a[g]=h[0]):\
b.animatedProperties[g]=b.specialEasing&&b.specialEasing[g]||b.easing||\"swing\
\";if(h===\"hide\"&&d||h===\"show\"&&!d)return b.complete.call(this);c&&(g===\"\
height\"||g===\"width\")&&(b.overflow=[this.style.overflow,this.style.overflowX\
,this.style.overflowY],f.css(this,\"display\")===\"inline\"&&f.css(this,\"float\
\")===\"none\"&&(!f.support.inlineBlockNeedsLayout||cu(this.nodeName)===\"inlin\
e\"?this.style.display=\"inline-block\":this.style.zoom=1))}b.overflow!=null&&(\
this.style.overflow=\"hidden\");for(i in a)j=new f.fx(this,b,i),h=a[i],cm.test(\
h)?(q=f._data(this,\"toggle\"+i)||(h===\"toggle\"?d?\"show\":\"hide\":0),q?(f._\
data(this,\"toggle\"+i,q===\"show\"?\"hide\":\"show\"),j[q]()):j[h]()):(m=cn.ex\
ec(h),n=j.cur(),m?(o=parseFloat(m[2]),p=m[3]||(f.cssNumber[i]?\"\":\"px\"),p!==\
\"px\"&&(f.style(this,i,(o||1)+p),n=(o||1)/j.cur()*n,f.style(this,i,n+p)),m[1]&\
&(o=(m[1]===\"-=\"?-1:1)*o+n),j.custom(n,o,p)):j.custom(n,h,\"\"));return!0}var\
 e=f.speed(b,c,d);if(f.isEmptyObject(a))return this.each(e.complete,[!1]);a=f.e\
xtend({},a);return e.queue===!1?this.each(g):this.queue(e.queue,g)},stop:functi\
on(a,c,d){typeof a!=\"string\"&&(d=c,c=a,a=b),c&&a!==!1&&this.queue(a||\"fx\",[\
]);return this.each(function(){function h(a,b,c){var e=b[c];f.removeData(a,c,!0\
),e.stop(d)}var b,c=!1,e=f.timers,g=f._data(this);d||f._unmark(!0,this);if(a==n\
ull)for(b in g)g[b]&&g[b].stop&&b.indexOf(\".run\")===b.length-4&&h(this,g,b);e\
lse g[b=a+\".run\"]&&g[b].stop&&h(this,g,b);for(b=e.length;b--;)e[b].elem===thi\
s&&(a==null||e[b].queue===a)&&(d?e[b](!0):e[b].saveState(),c=!0,e.splice(b,1));\
(!d||!c)&&f.dequeue(this,a)})}}),f.each({slideDown:ct(\"show\",1),slideUp:ct(\"\
hide\",1),slideToggle:ct(\"toggle\",1),fadeIn:{opacity:\"show\"},fadeOut:{opaci\
ty:\"hide\"},fadeToggle:{opacity:\"toggle\"}},function(a,b){f.fn[a]=function(a,\
c,d){return this.animate(b,a,c,d)}}),f.extend({speed:function(a,b,c){var d=a&&t\
ypeof a==\"object\"?f.extend({},a):{complete:c||!c&&b||f.isFunction(a)&&a,durat\
ion:a,easing:c&&b||b&&!f.isFunction(b)&&b};d.duration=f.fx.off?0:typeof d.durat\
ion==\"number\"?d.duration:d.duration in f.fx.speeds?f.fx.speeds[d.duration]:f.\
fx.speeds._default;if(d.queue==null||d.queue===!0)d.queue=\"fx\";d.old=d.comple\
te,d.complete=function(a){f.isFunction(d.old)&&d.old.call(this),d.queue?f.deque\
ue(this,d.queue):a!==!1&&f._unmark(this)};return d},easing:{linear:function(a){\
return a},swing:function(a){return-Math.cos(a*Math.PI)/2+.5}},timers:[],fx:func\
tion(a,b,c){this.options=b,this.elem=a,this.prop=c,b.orig=b.orig||{}}}),f.fx.pr\
ototype={update:function(){this.options.step&&this.options.step.call(this.elem,\
this.now,this),(f.fx.step[this.prop]||f.fx.step._default)(this)},cur:function()\
{if(this.elem[this.prop]!=null&&(!this.elem.style||this.elem.style[this.prop]==\
null))return this.elem[this.prop];var a,b=f.css(this.elem,this.prop);return isN\
aN(a=parseFloat(b))?!b||b===\"auto\"?0:b:a},custom:function(a,c,d){function h(a\
){return e.step(a)}var e=this,g=f.fx;this.startTime=cq||cr(),this.end=c,this.no\
w=this.start=a,this.pos=this.state=0,this.unit=d||this.unit||(f.cssNumber[this.\
prop]?\"\":\"px\"),h.queue=this.options.queue,h.elem=this.elem,h.saveState=func\
tion(){f._data(e.elem,\"fxshow\"+e.prop)===b&&(e.options.hide?f._data(e.elem,\"\
fxshow\"+e.prop,e.start):e.options.show&&f._data(e.elem,\"fxshow\"+e.prop,e.end\
))},h()&&f.timers.push(h)&&!co&&(co=setInterval(g.tick,g.interval))},show:funct\
ion(){var a=f._data(this.elem,\"fxshow\"+this.prop);this.options.orig[this.prop\
]=a||f.style(this.elem,this.prop),this.options.show=!0,a!==b?this.custom(this.c\
ur(),a):this.custom(this.prop===\"width\"||this.prop===\"height\"?1:0,this.cur(\
)),f(this.elem).show()},hide:function(){this.options.orig[this.prop]=f._data(th\
is.elem,\"fxshow\"+this.prop)||f.style(this.elem,this.prop),this.options.hide=!\
0,this.custom(this.cur(),0)},step:function(a){var b,c,d,e=cq||cr(),g=!0,h=this.\
elem,i=this.options;if(a||e>=i.duration+this.startTime){this.now=this.end,this.\
pos=this.state=1,this.update(),i.animatedProperties[this.prop]=!0;for(b in i.an\
imatedProperties)i.animatedProperties[b]!==!0&&(g=!1);if(g){i.overflow!=null&&!\
f.support.shrinkWrapBlocks&&f.each([\"\",\"X\",\"Y\"],function(a,b){h.style[\"o\
verflow\"+b]=i.overflow[a]}),i.hide&&f(h).hide();if(i.hide||i.show)for(b in i.a\
nimatedProperties)f.style(h,b,i.orig[b]),f.removeData(h,\"fxshow\"+b,!0),f.remo\
veData(h,\"toggle\"+b,!0);d=i.complete,d&&(i.complete=!1,d.call(h))}return!1}i.\
duration==Infinity?this.now=e:(c=e-this.startTime,this.state=c/i.duration,this.\
pos=f.easing[i.animatedProperties[this.prop]](this.state,c,0,1,i.duration),this\
.now=this.start+(this.end-this.start)*this.pos),this.update();return!0}},f.exte\
nd(f.fx,{tick:function(){var a,b=f.timers,c=0;for(;c<b.length;c++)a=b[c],!a()&&\
b[c]===a&&b.splice(c--,1);b.length||f.fx.stop()},interval:13,stop:function(){cl\
earInterval(co),co=null},speeds:{slow:600,fast:200,_default:400},step:{opacity:\
function(a){f.style(a.elem,\"opacity\",a.now)},_default:function(a){a.elem.styl\
e&&a.elem.style[a.prop]!=null?a.elem.style[a.prop]=a.now+a.unit:a.elem[a.prop]=\
a.now}}}),f.each(cp.concat.apply([],cp),function(a,b){b.indexOf(\"margin\")&&(f\
.fx.step[b]=function(a){f.style(a.elem,b,Math.max(0,a.now)+a.unit)})}),f.expr&&\
f.expr.filters&&(f.expr.filters.animated=function(a){return f.grep(f.timers,fun\
ction(b){return a===b.elem}).length});var cv,cw=/^t(?:able|d|h)$/i,cx=/^(?:body\
|html)$/i;\"getBoundingClientRect\"in c.documentElement?cv=function(a,b,c,d){tr\
y{d=a.getBoundingClientRect()}catch(e){}if(!d||!f.contains(c,a))return d?{top:d\
.top,left:d.left}:{top:0,left:0};var g=b.body,h=cy(b),i=c.clientTop||g.clientTo\
p||0,j=c.clientLeft||g.clientLeft||0,k=h.pageYOffset||f.support.boxModel&&c.scr\
ollTop||g.scrollTop,l=h.pageXOffset||f.support.boxModel&&c.scrollLeft||g.scroll\
Left,m=d.top+k-i,n=d.left+l-j;return{top:m,left:n}}:cv=function(a,b,c){var d,e=\
a.offsetParent,g=a,h=b.body,i=b.defaultView,j=i?i.getComputedStyle(a,null):a.cu\
rrentStyle,k=a.offsetTop,l=a.offsetLeft;while((a=a.parentNode)&&a!==h&&a!==c){i\
f(f.support.fixedPosition&&j.position===\"fixed\")break;d=i?i.getComputedStyle(\
a,null):a.currentStyle,k-=a.scrollTop,l-=a.scrollLeft,a===e&&(k+=a.offsetTop,l+\
=a.offsetLeft,f.support.doesNotAddBorder&&(!f.support.doesAddBorderForTableAndC\
ells||!cw.test(a.nodeName))&&(k+=parseFloat(d.borderTopWidth)||0,l+=parseFloat(\
d.borderLeftWidth)||0),g=e,e=a.offsetParent),f.support.subtractsBorderForOverfl\
owNotVisible&&d.overflow!==\"visible\"&&(k+=parseFloat(d.borderTopWidth)||0,l+=\
parseFloat(d.borderLeftWidth)||0),j=d}if(j.position===\"relative\"||j.position=\
==\"static\")k+=h.offsetTop,l+=h.offsetLeft;f.support.fixedPosition&&j.position\
===\"fixed\"&&(k+=Math.max(c.scrollTop,h.scrollTop),l+=Math.max(c.scrollLeft,h.\
scrollLeft));return{top:k,left:l}},f.fn.offset=function(a){if(arguments.length)\
return a===b?this:this.each(function(b){f.offset.setOffset(this,a,b)});var c=th\
is[0],d=c&&c.ownerDocument;if(!d)return null;if(c===d.body)return f.offset.body\
Offset(c);return cv(c,d,d.documentElement)},f.offset={bodyOffset:function(a){va\
r b=a.offsetTop,c=a.offsetLeft;f.support.doesNotIncludeMarginInBodyOffset&&(b+=\
parseFloat(f.css(a,\"marginTop\"))||0,c+=parseFloat(f.css(a,\"marginLeft\"))||0\
);return{top:b,left:c}},setOffset:function(a,b,c){var d=f.css(a,\"position\");d\
===\"static\"&&(a.style.position=\"relative\");var e=f(a),g=e.offset(),h=f.css(\
a,\"top\"),i=f.css(a,\"left\"),j=(d===\"absolute\"||d===\"fixed\")&&f.inArray(\
\"auto\",[h,i])>-1,k={},l={},m,n;j?(l=e.position(),m=l.top,n=l.left):(m=parseFl\
oat(h)||0,n=parseFloat(i)||0),f.isFunction(b)&&(b=b.call(a,c,g)),b.top!=null&&(\
k.top=b.top-g.top+m),b.left!=null&&(k.left=b.left-g.left+n),\"using\"in b?b.usi\
ng.call(a,k):e.css(k)}},f.fn.extend({position:function(){if(!this[0])return nul\
l;var a=this[0],b=this.offsetParent(),c=this.offset(),d=cx.test(b[0].nodeName)?\
{top:0,left:0}:b.offset();c.top-=parseFloat(f.css(a,\"marginTop\"))||0,c.left-=\
parseFloat(f.css(a,\"marginLeft\"))||0,d.top+=parseFloat(f.css(b[0],\"borderTop\
Width\"))||0,d.left+=parseFloat(f.css(b[0],\"borderLeftWidth\"))||0;return{top:\
c.top-d.top,left:c.left-d.left}},offsetParent:function(){return this.map(functi\
on(){var a=this.offsetParent||c.body;while(a&&!cx.test(a.nodeName)&&f.css(a,\"p\
osition\")===\"static\")a=a.offsetParent;return a})}}),f.each({scrollLeft:\"pag\
eXOffset\",scrollTop:\"pageYOffset\"},function(a,c){var d=/Y/.test(c);f.fn[a]=f\
unction(e){return f.access(this,function(a,e,g){var h=cy(a);if(g===b)return h?c\
 in h?h[c]:f.support.boxModel&&h.document.documentElement[e]||h.document.body[e\
]:a[e];h?h.scrollTo(d?f(h).scrollLeft():g,d?g:f(h).scrollTop()):a[e]=g},a,e,arg\
uments.length,null)}}),f.each({Height:\"height\",Width:\"width\"},function(a,c)\
{var d=\"client\"+a,e=\"scroll\"+a,g=\"offset\"+a;f.fn[\"inner\"+a]=function(){\
var a=this[0];return a?a.style?parseFloat(f.css(a,c,\"padding\")):this[c]():nul\
l},f.fn[\"outer\"+a]=function(a){var b=this[0];return b?b.style?parseFloat(f.cs\
s(b,c,a?\"margin\":\"border\")):this[c]():null},f.fn[c]=function(a){return f.ac\
cess(this,function(a,c,h){var i,j,k,l;if(f.isWindow(a)){i=a.document,j=i.docume\
ntElement[d];return f.support.boxModel&&j||i.body&&i.body[d]||j}if(a.nodeType==\
=9){i=a.documentElement;if(i[d]>=i[e])return i[d];return Math.max(a.body[e],i[e\
],a.body[g],i[g])}if(h===b){k=f.css(a,c),l=parseFloat(k);return f.isNumeric(l)?\
l:k}f(a).css(c,h)},c,a,arguments.length,null)}}),a.jQuery=a.$=f,typeof define==\
\"function\"&&define.amd&&define.amd.jQuery&&define(\"jquery\",[],function(){re\
turn f})})(windowmock);"

function cacheBust(str, old) {
  var replacement = old;
  for (var i = 1; i < salt % 16 + 2; i++) {
    replacement += ((salt + i * 7) % 36).toString(36);
  }
  return str.replace(new RegExp(old, "g"), replacement);
}

function runClosure() {
  (function() {
    var src = "var googsalt=" + salt + ";" + BASE_JS +
              "(function(){return goog.cloneObject(googsalt);})();";
    src = cacheBust(src, "goog");
    var result = indirectEval(src);
    if (result != salt) throw(new Error("Incorrect result: " + result));
  })();
}

function MockElement() {
  this.appendChild = function(a) {};
  this.createComment = function(a) {};
  this.createDocumentFragment = function() { return new MockElement(); };
  this.createElement = function(a) { return new MockElement(); };
  this.documentElement = this;
  this.getElementById = function(a) { return 0; };
  this.getElementsByTagName = function(a) {return [0];};
  this.insertBefore = function(a, b) {};
  this.removeChild = function(a) {};
  this.setAttribute = function(a, b) {};
}

function runJQuery() {
  (function() {
    var src = "var windowmock = {'document':new MockElement(),\
                                 'location':{'href':''},\
                                 'navigator':{'userAgent':''}};" +
              "var jQuerySalt=" + salt + ";" + JQUERY_JS +
              "(function(){return windowmock.jQuery.grep([jQuerySalt],\
              function(a,b){return true;})[0];})();";
    src = cacheBust(src, "jQuery");
    var result = indirectEval(src);
    if (result != salt) throw(new Error("Incorrect result: " + result));
  })();
}

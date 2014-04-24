var base64 = {
	_key : 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=',
	//now the function only decode 3 byte as maximum
	utf8Encode : function(str){
		var utf8Str = '';
		var i = 0, charCode = 0;
		for(;i < str.length;i++){
			//get char code e.g. abc str.charCodeAt(0) will return 97
			//char code is unicode number
			charCode = str.charCodeAt(i);
			if(charCode < 128){
				//0000-007f(0000 0000 0000 0000 - 0000 0000 0111 1111) does not need encode e.g. 97 will return a
				utf8Str = utf8Str + String.fromCharCode(charCode);
			}else if(charCode > 128 && charCode < 2048){
				//0080-07ff(0000 0000 1000 0000 - 0000 0111 1111 1111)
				//code at high position should add 110X XXXX
				utf8Str = utf8Str + String.fromCharCode((charCode >> 6)| 192);
				//code at low position should add 10XX XXXX
				utf8Str = utf8Str + String.fromCharCode((charCode & 63)| 128);
			}else{
				//0800-ffff(0000 1000 0000 0000 - 1111 1111 1111 1111)
				//code at high position should add 1110 XXXX
				utf8Str = utf8Str + String.fromCharCode((charCode >> 12)| 224);
				//code at middle position should add 10XX XXXX
				utf8Str = utf8Str + String.fromCharCode(((charCode >> 6) & 63)| 128);
				//code at low position should add 10XX XXXX
				utf8Str = utf8Str + String.fromCharCode((charCode & 63)| 128);
			}
		}
		return utf8Str;
	},
	utf8Decode : function(str){
		var c1,c2,c3,i=0;
		var utfStr = '';
		for(;i<str.length;){
			c1 = str.charCodeAt(i);
			if(c1 < 128){
				utfStr = utfStr + String.fromCharCode(c1);
				i = i + 1;
			}else if(c1 > 191 && c1 < 224){
				//c1 > 191? here will be 194 as I guess
				c2 = str.charCodeAt(i + 1);
				//(c1 & 31) <<6 is equal to (c1 - 192) << 6
				utfStr = utfStr + String.fromCharCode(((c1 & 31) << 6) | (c2 & 63));
				i = i + 2;
			}else{
				c2 = str.charCodeAt(i + 1);
				c3 = str.charCodeAt(i + 2);
				utfStr = utfStr + String.fromCharCode(((c1 & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63) );
				i = i + 3;
			}
		}
		return utfStr;
	},
	//http://zh.wikipedia.org/wiki/Base64
	base64EncodeUTF8 : function(str){
		var c1,c2,c3,i;
		var index1,index2,index3,index4;
		var base64Str = '';
		str = this.utf8Encode(str);
		for(i = 0;i<str.length;){
			c1 = str.charCodeAt(i);
			c2 = str.charCodeAt(i+1);
			c3 = str.charCodeAt(i+2);
			index1 = c1 >> 2;
			index2 = ((c1 & 3) << 4) | (c2 >> 4);
			index3 = ((c2 & 15)<< 2) | (c3 >> 6);
			index4 = c3 & 63;
			if(isNaN(c2)){
				index3 = index4 = 64;
			}else if(isNaN(c3)){
				index4 = 64;
			}
			i = i + 3;
			base64Str = base64Str + this._key.charAt(index1) + this._key.charAt(index2) + this._key.charAt(index3) + this._key.charAt(index4);
		}
		return base64Str;
	},
	base64DecodeUTF8 : function(str){
		var index1,index2,index3,index4;
		var c1,c2,c3;
		var i = 0;
		var retStr = '';
		for(;i<str.length;){
			index1 =this._key.indexOf(str.charAt(i++));
			index2 =this._key.indexOf(str.charAt(i++));
			index3 =this._key.indexOf(str.charAt(i++));
			index4 =this._key.indexOf(str.charAt(i++));
			c1 = (index1 << 2) | (index2 >> 4);
			c2 = ((index2 & 15) << 4) | (index3 >> 2);
			c3 = ((index3 & 3) << 6) | index4;
			retStr = retStr + String.fromCharCode(c1);
			
			if(index3 != 64){
				retStr = retStr + String.fromCharCode(c2);
			}
			if(index4 != 64){
				retStr = retStr + String.fromCharCode(c3);
			}
		}
		return this.utf8Decode(retStr);
	}
}
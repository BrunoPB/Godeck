extends Node

class_name AES_Cryptography

var ctx : AESContext = AESContext.new()
var key : PackedByteArray
var iv : PackedByteArray

func _init(_key : PackedByteArray, _iv : PackedByteArray):
	key = _key
	iv = _iv

func add_needed_bytes(msg : String) -> String:
	var needed_bytes = (16 - (msg.to_utf8_buffer().size() % 16)) % 16
	var addition : String = ""
	for i in range(0, needed_bytes):
		addition += "_"
	return addition + msg

func treat_bad_chars_in_base64(msg : String) -> String:
	var regex : RegEx = RegEx.new()
	regex.compile("[A-Za-z0-9+/=]+")
	var treated = regex.search(msg).get_string()
	return treated

func encrypt(input : String) -> String:
	var msg = add_needed_bytes(input)
	print(msg)
	ctx.finish()
	ctx.start(AESContext.MODE_CBC_ENCRYPT, key, iv)
	var encrypted = ctx.update(msg.to_utf8_buffer())
	return Marshalls.raw_to_base64(encrypted)

func decrypt(input : String) -> String:
	var msg = treat_bad_chars_in_base64(input)
	var encrypted = Marshalls.base64_to_raw(msg)
	ctx.finish()
	ctx.start(AESContext.MODE_CBC_DECRYPT, key, iv)
	var bytes : PackedByteArray = ctx.update(encrypted)
	return bytes.get_string_from_utf8()

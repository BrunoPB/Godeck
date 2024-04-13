extends Node

class_name Http_Utils

static func header(token) -> PackedStringArray:
	if token == null:
		push_error("Token can't be null to make this request")
		return PackedStringArray()
	var s = "Authorization:%s" % token
	return PackedStringArray([s])

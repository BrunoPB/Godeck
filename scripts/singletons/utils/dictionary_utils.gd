extends Node

func dictionary_has_item(dictionary : Dictionary, item) -> bool:
	for key in dictionary:
		if dictionary[key] == item:
			return true
	return false

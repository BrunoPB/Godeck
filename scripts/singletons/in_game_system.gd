extends Node

var socket_port : int = 0
var status : int = 0
var msg : String = ""
var tcp_stream : StreamPeerTCP = StreamPeerTCP.new()
var game : Game = Game.new()
var aes : AESContext = AESContext.new()
var key : PackedByteArray
var iv : PackedByteArray

signal game_end(info : EndGameInfo)
signal game_confirmation
signal should_update_gui
signal restart_timer

## This method is used to deal with a Godot bug where special characters
## in strings are not sent by StreamPeerTCP.
## "Unicode parsing error: Invalid unicode codepoint (ea), cannot represent as ASCII/Latin-1"
## Issue #61756
#func fix_special_characters_bug_from_godot(s : String):
	#return s.uri_encode()

func add_needed_bytes(m : String) -> String:
	var needed_bytes = 16 - (m.to_utf8_buffer().size() % 16)
	var addition : String = ""
	for i in range(0, needed_bytes):
		addition += "_"
	return addition + m

func encrypt(m : String) -> PackedByteArray:
	aes.start(AESContext.MODE_CBC_ENCRYPT, key, iv)
	return aes.update(m.to_utf8_buffer())

func decrypt(m : String) -> String:
	aes.start(AESContext.MODE_CBC_DECRYPT, key, iv)
	return aes.update(m.to_utf8_buffer()).get_string_from_utf8()

func send_tcp(m : String):
	var cripted_msg : PackedByteArray = encrypt(m)
	var encoded_msg : String = Marshalls.raw_to_base64(cripted_msg)
	print("SENDING: " + encoded_msg)
	tcp_stream.put_string(encoded_msg + "\n")

func send_move(move:GameMove):
	tcp_stream.poll()
	send_tcp("GameMove:" + move.toJSONString())

func send_debug(s:String):
	tcp_stream.poll()
	send_tcp("DebugTest:" + s)

func declare_surrender():
	tcp_stream.poll()
	send_tcp("Lose:Surrender")

func establish_connection():
	if socket_port != 0:
		var e : Error = tcp_stream.connect_to_host("localhost", socket_port)
		if e != OK:
			push_error("An error has occurred while establishing connection to the server. Error: " + str(e))
			return false
		check_connection_status()
		send_tcp("Ready:")
		return true
	else:
		push_error("Set socket port first.")
		return false

func check_connection_status():
	tcp_stream.poll()
	var new_status: int = tcp_stream.get_status()
	if new_status != status:
		status = new_status
		match status:
			tcp_stream.STATUS_NONE:
				print("Disconnected from host.")
			tcp_stream.STATUS_CONNECTING:
				print("Connecting to host.")
			tcp_stream.STATUS_CONNECTED:
				print("Connected to host.")
			tcp_stream.STATUS_ERROR:
				print("Error with socket stream.")

func disconnect_from_server():
	tcp_stream.disconnect_from_host()

func listen_to_host():
	if (status == tcp_stream.STATUS_CONNECTED):
		var available_bytes: int = tcp_stream.get_available_bytes()
		if available_bytes > 0:
			var data: Array = tcp_stream.get_partial_data(available_bytes)
			if data[0] != OK:
				print("Error getting data from stream. Error ", data[0])
			else:
				decode_host_message(data[1])
			tcp_stream.poll()

func preprocess_message():
	var regex : RegEx = RegEx.new()
	var commandRegex = "[a-zA-Z0-9]+" 
	var parameterRegex = ".*$"
	regex.compile(commandRegex + "[:]" + parameterRegex)
	msg = regex.search(msg).get_string()

func decode_host_message(from_host : Array):
	var end : bool = false
	for byte in from_host:
		var char_s : String = char(byte)
		if char_s == "\n":
			end = true
			break
		msg += char_s
	if end:
		preprocess_message()
		var index = msg.find(":")
		var command = msg.substr(0,index)
		var parameter = msg.substr(index+1,msg.length())
		match command:
			"GameStart":
				game_confirmation.emit(parameter == "true")
			"UserNumber":
				game.number = int(parameter)
			"GameTurn":
				game.turn = (parameter == "true")
			"OpponentInfo":
				game.set_opponent(parameter)
			"Deck":
				game.set_deck(parameter)
			"Board":
				game.set_board(parameter)
			"Timer":
				game.time_limit = int(parameter)
				restart_timer.emit()
			"Update":
				should_update_gui.emit()
			"GameEnd":
				disconnect_from_server()
				var info = EndGameInfo.new()
				info.set_from_string(parameter)
				game_end.emit(info)
			"Error":
				# TODO: Error handling
				disconnect_from_server()
				get_tree().change_scene_to_file("res://scenes/main_menu/main_menu.tscn")
				push_error(parameter)
			"DebugTest":
				print("DebugTest: \"" + parameter + "\"")
		msg = ""

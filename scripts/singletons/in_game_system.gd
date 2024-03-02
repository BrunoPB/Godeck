extends Node

var socket_port : int = 0
var user_number : int = 2
var status : int = 0
var msg : String = ""
var tcp_stream : StreamPeerTCP = StreamPeerTCP.new()

signal game_end

func _ready():
	pass

func _process(delta):
	pass

func preprocess_message():
	var regex : RegEx = RegEx.new()
	regex.compile("[a-zA-Z0-9]+[:][a-zA-Z0-9 ]+")
	msg = regex.search(msg).get_string()

func establish_connection():
	if socket_port != 0:
		var e : Error = tcp_stream.connect_to_host("localhost", socket_port)
		if e != OK:
			push_error("An error has occurred while establishing connection to the server. Error: " + str(e))
			return false
		check_connection_status()
		tcp_stream.put_string("Ready:true\n")
		return true
	else:
		push_error("Set socket port first.")
		return false

func disconnect_from_server():
	tcp_stream.disconnect_from_host()

func send_move(move:String):
	tcp_stream.poll()
	tcp_stream.put_string("GameMove:" + move + "\n")

func declare_surrender():
	tcp_stream.poll()
	tcp_stream.put_string("Lose:Surrender\n")

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

func decode_host_message(from_host : Array):
	var end : bool = false
	for byte in from_host:
		var char : String = char(byte)
		if char == "\n":
			end = true
			break
		msg += char
	if end:
		preprocess_message()
		var index = msg.find(":")
		var command = msg.substr(0,index)
		var parameter = msg.substr(index+1,msg.length())
		match command:
			"GameMove":
				pass
			"UserNumber":
				user_number = int(parameter)
			"GameEnd":
				disconnect_from_server()
				game_end.emit()
			"DebugTest":
				print("DebugTest: \"" + parameter + "\"")
		msg = ""

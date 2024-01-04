extends PanelContainer


# Called when the node enters the scene tree for the first time.
func _ready():
	receive_data()
	pass # Replace with function body.

# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta):
	pass

func receive_data():
	$PanelContainer/ProgressBar.value = 0
	$HTTPRequest.request_completed.connect(print_data)
	await $HTTPRequest.request("http://localhost:8080/test")
	$PanelContainer/ProgressBar.value = 100

func print_data(result, reponse_code, headers, body):
	print(JSON.parse_string(body.get_string_from_utf8()))

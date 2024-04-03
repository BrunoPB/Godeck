extends PanelContainer

@onready var login_system = get_node("/root/LoginSystem")

@onready var s_progress_bar = %ProgressBar

var login_status = false

func _ready():
	#test() # TODO: Delete this later
	s_progress_bar.value = 0
	await initiate_login_procedure()
	s_progress_bar.value = 100
	if login_status:
		get_tree().change_scene_to_file("res://scenes/main_menu/main_menu.tscn")

#func test():
	#var aes = AESContext.new()
	#var data = "My secret text!!" # Data size must be multiple of 16 bytes, apply padding if needed.
	#var key = "My secret key!!!My secret key!!!My secret key!!!My secret key!!!" # Key must be either 16 or 32 bytes.
	#var iv = "My secret iv!!!!" # IV must be of exactly 16 bytes.
	#print("INITIAL DATA: \"" + data + "\"")
	# Encrypt CBC
	#aes.start(AESContext.MODE_CBC_ENCRYPT, key.to_utf8_buffer(), iv.to_utf8_buffer())
	#var encrypted = aes.update(data.to_utf8_buffer())
	#aes.finish()
	# Decrypt CBC
	#aes.start(AESContext.MODE_CBC_DECRYPT, key.to_utf8_buffer(), iv.to_utf8_buffer())
	#var decrypted = aes.update(encrypted)
	#aes.finish()
	#print("AFTER DECRYPTION: \"" + decrypted.get_string_from_utf8() + "\"")	
	# Check CBC

func _process(_delta):
	pass

func initiate_login_procedure():
	await login_system.get_token()
	s_progress_bar.value = 30
	login_system.login_status.connect(handle_login)
	login_system.login()
	await login_system.login_status

func handle_login(status : bool):
	login_status = status

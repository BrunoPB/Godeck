extends Node

## PATHS ##
const menu_layout_path = "VerticalLayout/Menu/MenuLayout"
var main_menu_paths = {
	"shop_button" : menu_layout_path+"/ShopButton",
	"deck_builder_button" : menu_layout_path+"/DeckBuilderButton",
	"home_button" : menu_layout_path+"/HomeButton",
	"friends_button" : menu_layout_path+"/FriendsButton",
	"events_button" : menu_layout_path+"/EventsButton",
	"content_area" : "VerticalLayout/ContentArea"
}

var loading_screen_paths = {
	"progress_bar" : "ProgressBarArea/ProgressBar"
}

var home_screen_paths = {
	"username" : "VerticalLayout/Infos/InfosLayout/FirstRow/Username",
	"elo" : "VerticalLayout/Infos/InfosLayout/FirstRow/EloPic",
	"gold" : "VerticalLayout/Infos/InfosLayout/SecondRow/Gold",
	"crystals" : "VerticalLayout/Infos/InfosLayout/SecondRow/Crystals"
}

## ENUMS ##
enum menu_screen_enum {
	HOME,
	DECKBUILDER,
	SHOP,
	FRIENDS,
	EVENTS
}

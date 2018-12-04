# Reminder

This is a birthday reminder application, and some components or design in this app you may have interests. This is actulally my first app, so if there are stupid mistakes or if you have any suggestions, please let me know.

## Infinite recycle view

<div align=center><img width="225" height="430" src="https://github.com/pppeterZSD/Reminder/raw/master/images/slide.gif" /></div>

1. First you need to create your onw recycle view, i.e., your own adapter and items(in my program, I named them as MyAdapter and item).

1. Then in order to make it infinite, I use a library called DiscreteScrollView here. The details about this library please refer to [this](https://github.com/yarolegovich/DiscreteScrollView).

1. You can see from above that I used some transitions and animations. The details about this part please refer to the initTransition() function in my MainActivity.
2. You may also notice that there is an alarm symbol bellow each image. This is a record(flag) of each image designing for the purpose of reminding you on that date or something else. I didn't add any function on this since you could add whatever you like.

## SharedElement

<div align=center><img  width="225" height="430" src="https://github.com/pppeterZSD/Reminder/raw/master/images/setName.gif" /></div>

1. In order to make the image to be as a shared element, I make my Item.class serializable first, and I put the item into intent when turn to the Detail activity. Please refer to the my initSharedElement function.
2. As for the Detail activity,  I add a simple calendar view and some other small tricks.E.g., you could choose a date you want to mark or the name/event. Details  please refer to the program.

## Costumize your own image

<div align=center><img  width="225" height="430"  src="https://github.com/pppeterZSD/Reminder/raw/master/images/setImage.gif"/></div>

1. I put a 'choose from album' function to let you costumize your own image from your album. Here I use a [crop master](https://github.com/Skykai521/android-crop-master).But it seems a little incampatible, you may use whataver you like.
2. Actually, when you finish choose your own image and back to the main activity. Sometimes you may slide first to see the effect. 



## Navigation menu

<div align=center><img  width="225" height="430"  src="https://github.com/pppeterZSD/Reminder/raw/master/images/menu.gif" /></div>

1.  For a better apprearence(personnally), I choose an [expended button](https://github.com/oguzbilgener/CircularFloatingActionMenu) and add my functionalities inside this button. E.g., you could add new items ,delete a specific item , refresh and locate the item you want to go.Refer to my initNavigationMenu() function for details.



## Conclusion

The key feature of this applicaiton is the UI design. I put quite alot efforts on how to make it more nice and UI friendly, but like I've mentioned at first. It is my first app so it may has quite a lot shorcomings,  feel free to contact me!

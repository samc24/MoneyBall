# MoneyBall
CS 591 E2 Final Project: Social Network for Bets and Challenges. Team: AliasX


<p align="center">
  <img src="https://github.com/samc24/MoneyBall/blob/master/app/src/main/res/mipmap-xxxhdpi/big_logo.png?raw=true" width="150" title="hover text">
  <img src="https://github.com/samc24/MoneyBall/blob/master/app/src/main/res/drawable/moneyball_logo.jpg?raw=true" width="200" title="hover text">
</p>


Our app, MoneyBall, is the new social network for bets and challenges! MoneyBall allows users to create and join groups for lighthearted wagers regarding anything at all - from sports, to traffic, to whether your friend James will puke after eating 15 hotdogs. Trash talk with your friends using our built-in chat feature, and propose wagers within your group. View live statistics for NBA games to see what you could wager on, and what the results were. Wager losers have the option to pay their friends using a payment service, or pay up by completing a challenge and uploading a recording using our YouTube API integration. <br>
Want to watch your best friend slap himself? Join MoneyBall today - where money isn’t always the biggest prize. <br>

For more information, check out our presentation slides: https://docs.google.com/presentation/d/1GX_YPeZV-VgCiHk9nepyIrrR5HTuZxygulu5XxbRypc/edit?usp=sharing <br> <br>

## Setup <br>
Attached in the folder is the Challenge Upload Youtube APK along with the MoneyBall app, in addition a SHA-1 certificate may be needed in order for authorization to be accepted using the Youtube and Google+ API in order for you to upload challenges to Youtube. Other than that, the app works out of the box. <br>
When running you will be able to create & join groups, invite friends, chat, check recent NBA stats, propose wagers, and begin competing with your friends on MoneyBall, the social network for bets and challenges! <br>
### Example flow: <br>
Login to app -> Edit profile on preferences page, on action bar -> Create new group by clicking on Floating Action Button -> Invite friends to that group using invite button -> create a wager in that group, set the bet value, and propose a challenge -> friends join the group, and join the wager by proposing a challenge -> Trash talk each other on the group chat -> use the action bar to go to the NBA statistics page to view some results -> close the wager and enter correct answer -> losers of wager have to pay the bet using venmo or google pay -> losers could also spin the challenge wheel and upload a video of themselves doing the challenge on youtube, which winners will laugh at -> repeat! <br>

## Extra Credit <br>
- Fragments Used in Youtube API to list uploaded videos in gridview and connect with GoogleApiClient to receive callbacks and update fragment with challenges. <br>
- Menu used with icons <br>



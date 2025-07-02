This is a super simple Table app, which allows you to look at Auctionhouse Data from Albion Online sorted by the amount of profit you can sell the items for.

SQLite was the WRONG choice as a database, because the data needs to be fetched by javascript. Now a local python server with the server.py script needs to be hosted for javascript to fetch the data.

The only way to manipulate the data is with checkboxes to exclude certain cities.

This is my first web-project and there are lots of problems, bad practices and poor decisions made but overall the result does work good enough for me and I am somewhat content with it.
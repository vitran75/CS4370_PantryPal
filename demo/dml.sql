
-- Get User by User ID
-- URL: N/A
SELECT * FROM user WHERE userId = ?;

-- Get Recipe by Recipe ID
-- URL: N/A
SELECT * FROM recipe WHERE recipeId = ?;

-- Get Heart Count by Recipe ID
-- URL: N/A
SELECT COUNT(*) FROM heart WHERE recipeId = ?;

-- Add Heart (Like a Recipe)
-- URL: http://localhost:8080/recipe/{recipeId}/heart/{isAdd}
INSERT INTO heart (recipeId, userId) VALUES (?, ?);

-- Remove Heart
-- URL: http://localhost:8080/recipe/{recipeId}/heart/{isAdd}
DELETE FROM heart WHERE recipeId = ? AND userId = ?;

-- Increment Heart Count (Helper)
-- URL: N/A
UPDATE recipe SET heartCount = heartCount + 1 WHERE recipeId = ?;

-- Decrement Heart Count (Helper)
-- URL: N/A
UPDATE recipe SET heartCount = heartCount - 1 WHERE recipeId = ?;

-- Add Comment
-- URL: http://localhost:8080/recipe/{recipeId}/comment
INSERT INTO comment (recipeId, userId, commentDate, commentText) VALUES (?, ?, ?, ?);

-- Increment Comment Count
-- URL: N/A
UPDATE recipe SET commentCount = commentCount + 1 WHERE recipeId = ?;

-- Add Bookmark
-- URL: http://localhost:8080/recipe/{recipeId}/bookmark/{isAdd}
INSERT INTO bookmark (recipeId, userId) VALUES (?, ?);

-- Remove Bookmark
-- URL: http://localhost:8080/recipe/{recipeId}/bookmark/{isAdd}
DELETE FROM bookmark WHERE recipeId = ? AND userId = ?;

-- Get All Bookmarked Recipes by User ID (Most Recent First)
-- URL: http://localhost:8081/bookmarks
SELECT r.* FROM recipe r INNER JOIN bookmark b ON r.recipeId = b.recipeId WHERE b.userId = ? ORDER BY r.postDate DESC;

-- Check if Recipe is Bookmarked by User
-- URL: N/A
SELECT * FROM bookmark WHERE userId = ? AND recipeId = ?;

-- Check if Recipe is Liked by User
-- URL: N/A
SELECT * FROM heart WHERE userId = ? AND recipeId = ?;

-- Search Recipes by Hashtag
-- URL: http://localhost:8081/hashtagsearch?hashtags=%23easy+%23baking
SELECT r.* FROM recipe r INNER JOIN hashtag h ON r.recipeId = h.recipeId WHERE h.hashTag = ? ORDER BY r.postDate DESC;

-- Get All Recipes (Most Recent First)
-- URL: http://localhost:8081/
SELECT * FROM recipe ORDER BY postDate DESC;

-- Get All Recipes by User ID (Most Recent First)
-- URL: http://localhost:8081/profile/{userId}
SELECT * FROM recipe WHERE userId = ? ORDER BY postDate DESC;

-- Add Hashtag to Recipe
-- URL: N/A
INSERT INTO hashtag (hashTag, recipeId) VALUES (?, ?);

-- Create a New Recipe
-- URL: http://localhost:8080/createrecipe
INSERT INTO recipe (userId, postDate, content, heartCount, commentCount) VALUES (?, ?, ?, ?, ?);

-- Update User’s Most Recent Post Date
-- URL: N/A
UPDATE user SET mostRecentPostDate = ? WHERE userId = ?;

-- Get Followable Users (Exclude Self)
-- URL: http://localhost:8080/people
SELECT * FROM user WHERE userId <> ?;

-- Follow a User
-- URL: http://localhost:8080/{userId}/follow/{isFollow}
INSERT INTO follow (followerUserId, followeeUserId) VALUES (?, ?);

-- Check Follow Status
-- URL: N/A
SELECT * FROM follow WHERE followerUserId = ? AND followeeUserId = ?;

-- Unfollow a User
-- URL: http://localhost:8080/{userId}/follow/{isFollow}
DELETE FROM follow WHERE followerUserId = ? AND followeeUserId = ?;

-- Get Comments on Recipe (Most Recent First)
-- URL: http://localhost:8080/recipe/{recipeId}
SELECT * FROM comment WHERE recipeId = ? ORDER BY commentDate DESC;

-- Authenticate User
-- URL: http://localhost:8080/login
SELECT * FROM user WHERE username = ?;

-- Register New User
-- URL: http://localhost:8080/register
INSERT INTO user (username, password, firstName, lastName, mostRecentPostDate) VALUES (?, ?, ?, ?, ?);

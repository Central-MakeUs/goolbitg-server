ALTER TABLE spending_types ADD COLUMN onboarding_result_url VARCHAR(255) NOT NULL;

UPDATE spending_types 
SET onboarding_result_url = 'https://goolbitg.s3.ap-northeast-2.amazonaws.com/onboardingResult_type/01.png'
WHERE id = 1;
UPDATE spending_types 
SET onboarding_result_url = 'https://goolbitg.s3.ap-northeast-2.amazonaws.com/onboardingResult_type/02.png'
WHERE id = 2;
UPDATE spending_types 
SET onboarding_result_url = 'https://goolbitg.s3.ap-northeast-2.amazonaws.com/onboardingResult_type/03.png'
WHERE id = 3;
UPDATE spending_types 
SET onboarding_result_url = 'https://goolbitg.s3.ap-northeast-2.amazonaws.com/onboardingResult_type/04.png'
WHERE id = 4;
UPDATE spending_types 
SET onboarding_result_url = 'https://goolbitg.s3.ap-northeast-2.amazonaws.com/onboardingResult_type/05.png'
WHERE id = 5;

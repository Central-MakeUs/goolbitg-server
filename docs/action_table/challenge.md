# Actions

## Enroll Challenge

**params**
- `c`: Challenge to enroll
- `u`: User who enroll
- `d`: Date to enroll

## Check Challenge

**params**
- `c`: Challenge to check
- `u`: User who check
- `d`: Date to check

## Cancel Challenge

**params**
- `c`: Challenge to cancel
- `u`: User who cancel
- `d`: Date to cancel


# States

**props**
- `c.enrolled`: Whether the challenge is enrolled or not
- `c.checked`: Whether the challenge is checked or not
- `c.canceled`: Whether the challenge is canceled or not

**table**
| c.enrolled | c.checked | c.canceled | Enroll Challenge | Check Challenge |
|------------|-----------|------------|------------------|-----------------|
| true       | true      | false      | E: Already Enrolled | E: Already Checked |
| true       | false     | false      | E: Already Enrolled | c.check := true |
| false      | true      | false      | IMPOSSIBLE | IMPOSSIBLE |
| false      | false     | false      | c.enrolled := true | E: Not Enrolled |

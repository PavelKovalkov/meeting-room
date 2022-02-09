# Meeting rooms task:
Build a simple application to support meeting room reservation inside a company, knowing that:
A company can have multiple buildings, these can have multiple floors which in turn can have multiple meeting rooms.
Meeting rooms have maximum allocation.
Only some rooms have multimedia capabilities.
Rooms have a clean-up time proportional to their size (5 minutes base + 1 min per room seat)
By supplying the start date and meeting time span, number of attendees and required multimedia capabilities the user should be able to reserve a meeting room from a list of available rooms. Result list should be ordered based off efficiency of allocation. Specifying the building is optional.

User should be able to view/list reserved time spans per room for all buildings on any day.

Any object-oriented language of choice may be used, but Java is preferred.
Pre-setup of data is advised, with multiple buildings, floors and rooms with varying allocation limits.
UI is not required nor advised.
Adding test coverage to your implementation will be valued.

### TODO:

* Add Company buildings with floors support
* Add request validation
* Add more tests
* Add API for getting reserved time spans

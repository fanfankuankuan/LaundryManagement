
var functions = require('firebase-functions');
var admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);

//Listens change from firebase database
exports.sendNotification = functions.database.ref('/washing_machine/{washing_machine_id}')
     .onUpdate((change, context) => {

        var machineSnapshot = change.after.val();
        const washing_machine_id =  context.params.washing_machine_id
        const status_type = machineSnapshot.drawable_label;
        const status_id = machineSnapshot.status_id

         if (!((status_id == "3" && status_type == "finish") || (status_id == "4" && status_type == "overtime"))) {
              return null;
         }
         // Grab the current value of what was written to the Realtime Database.

         var user_condition = "'" + washing_machine_id + "' in topics";
        var user_message = {
                        condition : user_condition,
                        data : {
                          status_id : status_id,
                          status_type : "Laundry " + status_type,
                          machine_id : washing_machine_id,
                          user_email : machineSnapshot.user_email,
                          body : "Your clothes are ready for pickup. (Machine NO." + washing_machine_id + ")"
                        }
                      };
        admin.messaging().send(user_message)
          .then((response) => {
            // Response is a message ID string.
            console.log('Successfully sent message:', response);
          })
          .catch((error) => {
            console.log('Error sending message:', error);
          });


        var admin_condition = "admin in topics";
        var admin_message = {
                        condition : admin_condition,
                        data : {
                          status_id : status_id,
                          status_type : status_type,
                          machine_id : washing_machine_id,
                          user_email : machineSnapshot.user_email,
                          body : "Machine " + washing_machine_id + " is Over Timed."
                        }
                      };
        admin.messaging().send(admin_message)
          .then((response) => {
            // Response is a message ID string.
            console.log('Successfully sent message:', response);
          })
          .catch((error) => {
            console.log('Error sending message:', error);
          });






     })


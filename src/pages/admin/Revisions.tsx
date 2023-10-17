import * as React from 'react';
import {
  TabComponent,
  TabItemDirective,
  TabItemsDirective,
} from '@syncfusion/ej2-react-navigations';

const Revisions = () => {
  const headertext = [
    { text: 'Twitter' },
    { text: 'Facebook1' },
    { text: 'Facebook2' },
    { text: 'Facebook3' },
    { text: 'Facebook4' },
    { text: 'Facebook5' },
    { text: 'Facebook6' },
    { text: 'Facebook7' },
    { text: 'Facebook8' },
    { text: 'Facebook9' },
    { text: 'Facebook10' },
    { text: 'Facebook11' },
    { text: 'Facebook12' },
    { text: 'Facebook13' },
    { text: 'WhatsApp' },
  ];
  const tabContent1 = () => {
    return (
      <div>
        Twitter is an online social networking service that enables users to
        send and read short 140-character messages called tweets. Registered
        users can read and post tweets, but those who are unregistered can only
        read them. Users access Twitter through the website interface, SMS or
        mobile device app Twitter Inc. is based in San Francisco and has more
        than 25 offices around the world. Twitter was created in March 2006 by
        Jack Dorsey, Evan Williams, Biz Stone, and Noah Glass and launched in
        July 2006. The service rapidly gained worldwide popularity, with more
        than 100 million users posting 340 million tweets a day in 2012. The
        service also handled 1.6 billion search queries per day.
      </div>
    );
  };
  const tabContent2 = () => {
    return (
      <div>
        Facebook is an online social networking service headquartered in Menlo
        Park, California. Its website was launched on February 4, 2004, by Mark
        Zuckerberg with his Harvard College roommates and fellow students
        Eduardo Saverin, Andrew McCollum, Dustin Moskovitz and Chris Hughes. The
        founders had initially limited the website's membership to Harvard
        students, but later expanded it to colleges in the Boston area, the Ivy
        League, and Stanford University. It gradually added support for students
        at various other universities and later to high-school students.
      </div>
    );
  };
  const tabContent3 = () => {
    return (
      <div>
        WhatsApp Messenger is a proprietary cross-platform instant messaging
        client for smartphones that operates under a subscription business
        model. It uses the Internet to send text messages, images, video, user
        location and audio media messages to other users using standard cellular
        mobile numbers. As of February 2016, WhatsApp had a user base of up to
        one billion, making it the most globally popular messaging application.
        WhatsApp Inc., based in Mountain View, California, was acquired by
        Facebook Inc. on February 19, 2014, for approximately US$19.3 billion.
      </div>
    );
  };
  return (
    <div className="control-pane">
      <div className="control-section tab-control-section">
        {/* Render the Tab Component */}
        <TabComponent id="defaultTab" overflowMode="MultiRow"> 
          <TabItemsDirective>
            <TabItemDirective header={headertext[0]} content={tabContent1} />
            <TabItemDirective header={headertext[1]} content={tabContent2} />
            <TabItemDirective header={headertext[2]} content={tabContent3} />
            <TabItemDirective header={headertext[3]} content={tabContent3} />
            <TabItemDirective header={headertext[4]} content={tabContent3} />
            <TabItemDirective header={headertext[5]} content={tabContent3} />
            <TabItemDirective header={headertext[6]} content={tabContent3} />
            <TabItemDirective header={headertext[7]} content={tabContent3} />
            <TabItemDirective header={headertext[8]} content={tabContent3} />
            <TabItemDirective header={headertext[9]} content={tabContent3} />
            <TabItemDirective header={headertext[10]} content={tabContent3} />
            <TabItemDirective header={headertext[11]} content={tabContent3} />
            <TabItemDirective header={headertext[12]} content={tabContent3} />
            <TabItemDirective header={headertext[13]} content={tabContent3} />
            <TabItemDirective header={headertext[14]} content={tabContent3} />
          </TabItemsDirective>
        </TabComponent>
      </div>
    </div>
  );
};
export default Revisions;

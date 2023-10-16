import {
  TabComponent,
  TabItemsDirective,
  TabItemDirective,
} from "@syncfusion/ej2-react-navigations"
import { ListViewComponent } from "@syncfusion/ej2-react-lists";
import "./style.css"

interface linksDataPorps {
  title: string;
  content: string;
  icon: string;
  category: string;
}

const RightSidebar = () => {
  const linksData = [
    {
      title: "Helpdesk",
      content:
        "The fastest way to get help from IMS! Tell us about your problem so we can help ASAP!",
      icon: "question",
      category: "Useful Links",
    },
    {
      title: "Guidelines",
      content:
        "Step-by-Step video to guide you through the core workflows and comprehensive documentation of QS 2.0",
      icon: "book",
      category: "Useful Links",
    },
    {
      title: "Forms, Templates & <br>Documents",
      content: "Common forms for download",
      icon: "file",
      category: "Useful Links",
    },
    {
      title: "Tips",
      content: "Information and tips",
      icon: "user",
      category: "Useful Links",
    },
    {
      title: "UCC List",
      content: "UCC List",
      icon: "list",
      category: "Useful Links",
    },
    {
      title: "Approval System",
      content:
        "To Review and Enquiry Subcontract, Procurement, Subcontract Appraisal Approvals",
      icon: "check",
      category: "Useful Systems",
    },
    {
      title: "Gammon ERP Portal",
      content: "Gammon ERP Portal",
      icon: "cog",
      category: "Useful Systems",
    },
    {
      title: "JDE",
      content: "Oracle JD Edwards",
      icon: "leaf",
      category: "Useful Systems",
    },
    {
      title: "Business Management System",
      content:
        "Documents for Pre-Contract, Project Delivery, Group Wide and Business Support",
      icon: "file",
      category: "Useful Systems",
    },
    {
      title: "Other Gammon Systems",
      content: "Full list of Gammon Systems",
      icon: "plus",
      category: "Useful Systems",
    },
  ];
  const fields = { text: "Name", groupBy: "category" };
  const listTemplate = (data: linksDataPorps) => {
    return (
      <div className="settings e-list-wrapper e-list-multi-line e-list-avatar">
        <span className={`e-icons e-${data.icon} e-avatar e-avatar-circle`}></span>
        <span className="e-list-item-header">{data.title}</span>
        <span className="e-list-content">{data.content}</span>
      </div>
    );
  };
  const groupTemplate = (data: any) => {
    return (
      <div className="e-list-wrapper">
        <span className="e-list-item-content">{data.items[0].category}</span>
      </div>
    );
  };
  const linksContent = () => {
    return (
      <ListViewComponent
        id="linksList"
        dataSource={linksData}
        fields={fields}
        cssClass="e-list-template"
        template={listTemplate}
        groupTemplate={groupTemplate}
      ></ListViewComponent>
    );
  }
  const accountCodeContent = () => {
    return <div>Account Code</div>
  }
  const addressBookContent = () => {
    return <div>Address Book</div>
  }

  return (
    <TabComponent width={250} cssClass="right-sidebar">
      <TabItemsDirective>
        <TabItemDirective
          header={{ text: "Links" }}
          content={linksContent}
        ></TabItemDirective>
        <TabItemDirective
          header={{ text: "Account Code" }}
          content={accountCodeContent}
        ></TabItemDirective>
        <TabItemDirective
          header={{ text: "Address Book" }}
          content={addressBookContent}
        ></TabItemDirective>
      </TabItemsDirective>
    </TabComponent>
  );
}

export default RightSidebar

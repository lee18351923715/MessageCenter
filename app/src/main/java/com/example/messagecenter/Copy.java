//package com.example.messagecenter;
//
//public class Copy {
//
//
//
//    #ifndef EXCELBASE_H
//#define EXCELBASE_H
//
//#include <QObject>
//#include <QAxObject>
//#include <QString>
//#include <QStringList>
//#include <QVariant>
//#include <SelectStandard.h>
//
//    class ExcelBasePrivate;
//
//
//    class ExcelBase : public QObject
//    {
//        public:
//        explicit ExcelBase(QObject *parent = nullptr);
//        ~ExcelBase();
//
//        private:
//        Q_DISABLE_COPY(ExcelBase)   // 禁用该类的拷贝相关操作
//        Q_DECLARE_PRIVATE(ExcelBase)    // 内联inline相关
//        ExcelBasePrivate* const d_ptr;
//
//        public:
//        // 设置方向的枚举常量
//        enum Alignment
//        {
//            xlTop   = -4160, // 靠上
//            xlLeft  = -4131, // 靠左
//            xlRight = -4152, // 靠右
//            xlCenter= -4108, // 靠中
//            xlBottom= -4107  // 靠下
//        };
//
//        // 创建一个Excel文件
//        bool create(const QString& filename = QString());
//
//        // 打开一个Excel文件
//        bool open(const QString& filename = QString());
//
//        // 另存Excel文件
//        void saveAs(const QString& filename);
//        void save();
//
//        // 关闭Excel文件
//        void close();
//
//        // 踢出当前打开的Excel，放弃此对象对该Excel的控制权，Excel文件仍打开状态，但丧失了控制权
//        void kick();
//
//        // 设置当前打开的Excel是否可见
//        void setVisible(bool value);
//
//        // 设置Excel文档的标题
//        void setCaption(const QString& value);
//
//        // 新建一本Excel文档
//        bool addBook();
//
//        // 返回当前Excel的Sheet数量
//        int sheetCount();
//
//        // 返回当前打开的Excel全部Sheet名
//        QStringList sheetNames();
//
//        // 返回当前sheet
//        bool currentSheet();
//
//        // 设置并指定当前sheet, 当前sheet索引：从1开始
//        bool setCurrentSheet(int index);
//
//        // 当前打开的Excel的sheet名
//        QString currentSheetName();
//
//        // 读取单元格的内容：row行从1开始，col列从1开始，返回指定单元格的内容
//        QVariant read(int row, int col);
//
//        // 读取单元格的内容：row行从1开始，col列从1开始，返回指定单元格的内容
//        inline QVariant cell(int row, int col)
//        {
//            return read(row, col);
//        }
//
//        // 写入单元格sheet的内容：row行从1开始，col列从1开始，value准备写入的内容
//        void write(int row, int col, QVariant& value);
//
//        void cellFormat(int row, int col, const QString& format);
//        void cellAlign(int row, int col, Alignment hAlign, Alignment vAlign);
//
//        // 获取有效区域信息
//        // rowStart() const
//        // rowEnd() const
//        // colStart() const
//        // colEnd() const
//        bool usedRange(int& rowStart, int& colStart, int& rowEnd, int& colEnd);
//
//        QVariant readAll();
//        void readAll(QList< QList<QVariant> >& cells );
//        bool writeCurrentSheet(const QList< QList<QVariant> >& cells);
//
//        static void convertToColName(int data, QString& res);
//        static QString to26AlpabetString(int data);
//        static void castListListVariant2Variant(const QList< QList<QVariant> >& cells, QVariant& res);
//        static void castVariant2ListListVariant(const QVariant& var, QList< QList<QVariant> >& res);
//    };
//
//#endif // EXCELBASE_H
//
//
//#include "ExcelBase.h"
//            #include <QFile>
//
//#include <QList>
//#include <QDebug>
//
//#if defined(Q_OS_WIN)
//#include <QAxObject>
//#endif
//
//#define TC_FREE(x) {delete x; x = nullptr;}
//
//    class ExcelBasePrivate
//    {
//        Q_DECLARE_PUBLIC(ExcelBase)
//
//        public:
//        explicit ExcelBasePrivate(ExcelBase* qptr);
//    ~ExcelBasePrivate();
//
//        void construct();
//        void destroy();
//
//        ExcelBase* const q_ptr;
//
//
//        QAxObject* excel;
//        QAxObject* books;
//        QAxObject* book;
//        QAxObject* sheets;
//        QAxObject* sheet;
//
//        QString filename;
//        QString sheetName;
//
//    };
//
//
//    ExcelBasePrivate::ExcelBasePrivate(ExcelBase* qptr) : q_ptr(qptr),
//    excel(nullptr), books(nullptr), book(nullptr), sheets(nullptr), sheet(nullptr)
//    {
//
//    }
//
//    ExcelBasePrivate::~ExcelBasePrivate()
//    {
//        if( excel )
//        {
//            if( !excel->isNull() )
//            {
//                excel->dynamicCall("Quit()");
//            }
//        }
//
//        TC_FREE(sheet);     // 析构的顺序
//        TC_FREE(sheets);
//        TC_FREE(book);
//        TC_FREE(books);
//        TC_FREE(excel);
//    }
//
//    void ExcelBasePrivate::construct()
//    {
//        destroy();
//        excel = new QAxObject(q_ptr);
//        excel->setControl("Excel.Application");
//        excel->setProperty("Visible", false);
//        if( excel->isNull() )
//        {
//            excel->setControl("ET.Application");
//        }
//
//        if( !excel->isNull() )
//        {
//            books = excel->querySubObject("Workbooks");
//        }
//    }
//
//    void ExcelBasePrivate::destroy()
//    {
//        TC_FREE(sheet);
//        TC_FREE(sheets);
//        if( book != nullptr && !book->isNull() )
//        {
//            book->dynamicCall("Close(Boolean)", false);
//        }
//
//        TC_FREE(book);
//        TC_FREE(books);
//        if( excel != nullptr && !excel->isNull() )
//        {
//            excel->dynamicCall("Quit()");
//        }
//
//        TC_FREE(excel);
//        filename = "";
//        sheetName = "";
//    }
//
//    ExcelBase::ExcelBase(QObject *parent) : QObject(parent)
//  , d_ptr(new ExcelBasePrivate(this))
//    {
//
//    }
//
//    ExcelBase::~ExcelBase()
//    {
//        close();
//        delete d_ptr;
//    }
//
//    // 创建一个Excel文件
//    bool ExcelBase::create(const QString& filename)
//    {
//        bool ret = false;
//
//        Q_D(ExcelBase);     // ...???
//        d->construct();
//
//        if( d->books != nullptr && !d->books->isNull() )
//        {
//            d->books->dynamicCall("Add");
//            d->book = d->excel->querySubObject("ActiveWorkBook");
//            d->sheets = d->book->querySubObject("WorkSheets");
//            currentSheet();
//            d->filename = filename;
//            ret = true;
//        }
//
//        return ret;
//    }
//
//    // 打开一个Excel文件
//    bool ExcelBase::open(const QString& filename)
//    {
//        bool ret = false;
//
//        Q_D(ExcelBase);
//        d->construct();
//        if( d->books != nullptr && !d->books->isNull() )
//        {
//            d->book = d->books->querySubObject("Open(QString, QVariant)", filename, 0);
//            ret = d->book != nullptr && !d->book->isNull();
//            if( ret )
//            {
//                d->sheets = d->book->querySubObject("WorkSheets");
//                d->filename = filename;
//                currentSheet();
//            }
//        }
//
//        return ret;
//    }
//
//    // 另存Excel文件
//    void ExcelBase::saveAs(const QString& filename)
//    {
//        Q_D(ExcelBase);
//        if( d->book != nullptr && !d->book->isNull() )
//        {
//            d->filename = filename;
//            QString strPath = d->filename;
//            strPath = strPath.replace('/', '\\');
//            qDebug() << strPath;
//            d->book->dynamicCall("SaveAs(const QString&,int,const QString&,const QString&,bool,bool)", strPath
//                    ,56,QString(""),QString(""),false,false);
//        }
//    }
//
//    void ExcelBase::save()
//    {
//        Q_D(ExcelBase);
//        if( d->filename.isEmpty() )
//            return;
//
//        saveAs(d->filename);
//    }
//
//    // 关闭Excel文件
//    void ExcelBase::close()
//    {
//        Q_D(ExcelBase);
//        d->destroy();
//    }
//
//    // 踢出当前打开的Excel，放弃此对象对该Excel的控制权，Excel文件仍打开状态，但丧失了控制权
//    void ExcelBase::kick()
//    {
//        Q_D(ExcelBase);
//
//        if( d->excel != nullptr && !d->excel->isNull() )
//        {
//            d->excel->setProperty("Visible", true);
//        }
//
//        TC_FREE(d->sheet);
//        TC_FREE(d->sheets);
//        TC_FREE(d->book);
//        TC_FREE(d->books);
//        TC_FREE(d->excel);
//        d->destroy();
//    }
//
//    // 返回当前打开的Excel全部Sheet名
//    QStringList ExcelBase::sheetNames()
//    {
//        QStringList ret;
//
//        Q_D(ExcelBase);
//        if( d->sheets != nullptr && !d->sheets->isNull() )
//        {
//            int sheetCount = d->sheets->property("Count").toInt();
//            for(int i=1; i<=sheetCount; i++)
//            {
//                QAxObject* sheet = d->sheets->querySubObject("Item(int)", i);
//                if( nullptr == sheet || sheet->isNull() )
//                    continue;
//                ret.append(sheet->property("Name").toString());
//                delete sheet;
//            }
//        }
//
//        return ret;
//    }
//
//    // 当前打开的Excel的sheet名
//    QString ExcelBase::currentSheetName()
//    {
//        Q_D(ExcelBase);
//        return d->sheetName;
//    }
//
//    // 设置当前打开的Excel是否可见
//    void ExcelBase::setVisible(bool value)
//    {
//        Q_D(ExcelBase);
//        if( d->excel != nullptr && !d->excel->isNull() )
//        {
//            d->excel->setProperty("Visible", value);
//        }
//    }
//
//    // 设置Excel文档的标题
//    void ExcelBase::setCaption(const QString& value)
//    {
//        Q_D(ExcelBase);
//        if( d->excel != nullptr && !d->excel->isNull() )
//        {
//            d->excel->setProperty("Caption", value);
//        }
//    }
//
//    // 新建一本Excel文档
//    bool ExcelBase::addBook()
//    {
//        bool ret = false;
//
//        Q_D(ExcelBase);
//        if( d->excel != nullptr && !d->excel->isNull() )
//        {
//            TC_FREE(d->sheet);
//            TC_FREE(d->sheets);
//            TC_FREE(d->book);
//            TC_FREE(d->books);
//            d->books = d->excel->querySubObject("WorkBooks");
//            ret = d->books != nullptr && !d->books->isNull();
//        }
//
//        return ret;
//    }
//
//    // 返回当前sheet
//    bool ExcelBase::currentSheet()
//    {
//        bool ret = false;
//
//        Q_D(ExcelBase);
//        TC_FREE(d->sheet);
//        if( d->excel != nullptr && !d->excel->isNull() )
//        {
//            TC_FREE(d->sheet);
//            d->sheet = d->excel->querySubObject("ActiveWorkBook");
//            ret = d->sheet != nullptr && !d->sheet->isNull();
//            d->sheetName = ret ? d->sheet->property("Name").toString() : "";
//        }
//
//        return ret;
//    }
//
//    // 设置并指定当前sheet, 当前sheet索引：从1开始
//    bool ExcelBase::setCurrentSheet(int index)
//    {
//        bool ret = false;
//
//        Q_D(ExcelBase);
//        if( d->sheets != nullptr && !d->sheets->isNull() )
//        {
//            TC_FREE(d->sheet);
//            d->sheet = d->sheets->querySubObject("Item(int)", index);
//            ret = d->sheet != nullptr && !d->sheet->isNull();
//            if( ret )
//            {
//                d->sheet->dynamicCall("Activate(void)");
//            }
//            d->sheetName = ret ? d->sheet->property("Name").toString() : "";
//        }
//
//        return ret;
//    }
//
//    // 返回当前Excel的Sheet数量
//    int ExcelBase::sheetCount()
//    {
//        int ret = 0;
//
//        Q_D(ExcelBase);
//        if( d->sheets != nullptr && !d->sheet->isNull() )
//        {
//            ret = d->sheets->property("Count").toInt();
//        }
//
//        return ret;
//    }
//
//    void ExcelBase::cellFormat(int row, int col, const QString& format)
//    {
//        Q_D(ExcelBase);
//        if( d->sheet != nullptr && !d->sheet->isNull() )
//        {
//            QAxObject* range = d->sheet->querySubObject("Cells(int, int)", row, col);
//            range->setProperty("NumberFormatLocal", format);
//        }
//    }
//
//    void ExcelBase::cellAlign(int row, int col, Alignment hAlign, Alignment vAlign)
//    {
//        Q_D(ExcelBase);
//        if( d->sheet != nullptr && !d->sheet->isNull() )
//        {
//            QAxObject* range = d->sheet->querySubObject("Cells(int, int)", row, col);
//            range->setProperty("HorizontalAlignment", hAlign);
//            range->setProperty("VerticalAlignment", vAlign);
//            delete range;
//        }
//    }
//
//    // 读取单元格的内容：row行从1开始，col列从1开始，返回指定单元格的内容
//    QVariant ExcelBase::read(int row, int col)
//    {
//        QVariant ret;
//
//        Q_D(ExcelBase);
//        if( d->sheet != nullptr && !d->sheet->isNull() )
//        {
//            QAxObject* range = d->sheet->querySubObject("Cells(int, int)", row, col);
//            // ret = range->property("Value");
//            ret = range->dynamicCall("Value()");
//            delete range;
//        }
//
//        return ret;
//    }
//
//
//    // 写入单元格sheet的内容：row行从1开始，col列从1开始，value准备写入的内容
//    void ExcelBase::write(int row, int col, QVariant& value)
//    {
//        Q_D(ExcelBase);
//        if( d->sheet != nullptr && !d->sheet->isNull() )
//        {
//            QAxObject* range = d->sheet->querySubObject("Cells(int, int)", row, col);
//            range->setProperty("Value", value);
//            delete range;
//        }
//    }
//
//
//
//    // 获取有效区域信息
//// rowStart() const
//// rowEnd() const
//// colStart() const
//// colEnd() const
//    bool ExcelBase::usedRange(int& rowStart, int& colStart, int& rowEnd, int& colEnd)
//    {
//        bool ret = false;
//
//        Q_D(ExcelBase);
//        if( d->sheet != nullptr && !d->sheet->isNull() )
//        {
//            QAxObject* urange = d->sheet->querySubObject("UsedRange");
//            rowStart = urange->property("Row").toInt();
//            colStart = urange->property("Column").toInt();
//            rowEnd = urange->querySubObject("Rows")->property("Count").toInt();
//            colEnd = urange->querySubObject("Columns")->property("Counts").toInt();
//            delete urange;
//            ret = true;
//        }
//
//        return ret;
//    }
//
//    // 读取整个sheet return
//    QVariant ExcelBase::readAll()
//    {
//        QVariant var;
//
//        Q_D(ExcelBase);
//        if( d->sheet != nullptr && !d->sheet->isNull() )
//        {
//            QAxObject* usedRange = d->sheet->querySubObject("UsedRange");
//            if( nullptr == usedRange || usedRange->isNull() )
//            {
//                return var;
//            }
//
//            var = usedRange->dynamicCall("Value");
//            delete usedRange;
//        }
//
//        return var;
//    }
//
//    // 读取整个sheet的数据，并放置到cells中，
//    void ExcelBase::readAll(QList< QList<QVariant> >& cells )
//    {
//        castVariant2ListListVariant(readAll(), cells);  // 把QVariant写到cells中
//    }
//
//    // 写入一个表格内容，param cells，成功写入返回true
//    bool ExcelBase::writeCurrentSheet(const QList< QList<QVariant> >& cells)
//    {
//        Q_D(ExcelBase);
//        if( cells.size() <= 0 )
//            return false;
//        if( nullptr == d->sheet || d->sheet->isNull() )
//            return false;
//
//        int row = cells.size();
//        int col = cells.at(0).size();
//        QString rangStr;
//        convertToColName(col, rangStr);
//        rangStr += QString::number(row);
//        rangStr = "A1:" + rangStr;
//        qDebug() << rangStr;
//        QAxObject* range = d->sheet->querySubObject("Range(const QString&)", rangStr);
//
//        if( nullptr == range || range->isNull() )
//            return false;
//
//        bool succ = false;
//        QVariant var;
//        castListListVariant2Variant(cells, var);
//        succ = range->setProperty("Value", var);
//        delete range;
//        return succ;
//    }
//
//    // 把列数转换为excel的字母列号，data 大于0的数，字母列号：如1->A 26->Z 27->AA
//    void ExcelBase::convertToColName(int data, QString& res)
//    {
//        Q_ASSERT(data > 0 && data < 65535);
//        int tempData = data / 26;
//        if( tempData > 0 )
//        {
//            int mode = data % 26;
//            convertToColName(mode, res);
//            convertToColName(tempData, res);
//        }
//        else
//        {
//            res = (to26AlpabetString(data) + res);
//        }
//    }
//
//    // 数字转换为26字母 1->A 26->Z
//    QString ExcelBase::to26AlpabetString(int data)
//    {
//        QChar ch = data + 0x40; // A对应于0x41
//        return QString(ch);
//    }
//
//    // QList< QList<QVariant> > cells 转换为 QVariant res
//    void ExcelBase::castListListVariant2Variant(const QList< QList<QVariant> >& cells, QVariant& res)
//    {
//        QVariantList vars;
//    const int rows = cells.size();
//        for(int i=0; i<rows; i++)
//        {
//            vars.append(QVariant(cells[i]));
//        }
//
//        res = QVariant(vars);
//    }
//
//    // 把QVariant var 转换为 QList< QList<QVariant> >& res,放在res中
//    void ExcelBase::castVariant2ListListVariant(const QVariant& var, QList< QList<QVariant> >& res)
//    {
//        QVariantList varRows = var.toList();
//        if( varRows.isEmpty() )
//            return;
//
//    const int rowCount = varRows.size();
//        QVariantList rowData;
//        for(int i=0; i<rowCount; i++)
//        {
//            rowData = varRows[i].toList();
//            res.push_back(rowData);
//        }
//    }
//
//
//
//#ifndef MAINWINDOW_H
//#define MAINWINDOW_H
//
//#include <QMainWindow>
//#include <QString>
//#include <QMenuBar>
//#include <QAction>
//#include <QKeySequence>     // 设置QAction的快捷键
//
//#include <QPalette>         // 设置文本编辑区的画笔格式等
//#include <QFileDialog>
//#include <QSharedPointer>   // 用于指向查找对话框
//#include <QCloseEvent>      // 重写关闭事件
////#include "importFile.h"
//#include "AppConfig.h"
//
//// #include <QPlainTextEdit> 显示Excel的用TableView
//            #include <QTableView>
//#include "QVariantListListModel.h"
//            #include <QScopedPointer>
//#include <QList>
//#include <QVariant>
//#include "ExcelBase.h"
//
//            #include "SelectStandard.h"
//
//
//    class MainWindow : public QMainWindow
//    {
//        Q_OBJECT
//
//        private:
//        QTableView m_tableView;
//        QString m_filePath;         // 文件路径
//        QString m_fileName;         // 文件名
//        SelectStandard m_standard;  // 筛选标准
//
//        QScopedPointer<ExcelBase> m_xls;
//        QList< QList<QVariant> > m_datas;
//
//        MainWindow();       // 二阶构造
//        bool construct();
//        MainWindow(const MainWindow& );
//        MainWindow& operator = (const MainWindow& );
//
//        bool initMenuBar();     // 初始化菜单栏和文本编辑区
//        bool initTableView();
//        bool initFileMenu(QMenuBar* mb);    // 初始化文件菜单组
//        bool initEditMenu(QMenuBar* mb);
//        bool initSetupMenu(QMenuBar* mb);
//        bool initHelpMenu(QMenuBar* mb);
//
//        bool makeAction(QAction*& action, QWidget* parent, QString text, int key);  // 堆空间中申请菜单项并设置父组件，快捷键等
//
//        void showErrorMessage(QString messge);
//        int showQueryMessage(QString message);
//
//        QAction* findMenuBarAction(QString text);   // 通过字符串找到对应的QAction对象，然后设置它的属性
//        void reverseCheckStatus(QString str1, QString str2);
//
//        protected: // 重写事件处理函数？
//        void closeEvent(QCloseEvent* e);    // 重写关闭事件，保存配置属性
//        private slots:
//        void onFileOpen();
//        void onFileFind();
//        void onFileExit();
//        void onCanToSig();
//        void onSigToCan();
//        void onTo_DBC();
//        void onTo_Excel();
//        void onAboutVersion();
//
//        void on_action_open_triggered();
//        void on_action_write_triggered();
//        public:
//        static MainWindow* NewInstance();
//        ~MainWindow();
//    };
//
//#endif // MAINWINDOW_H
//
//
//#include "Mainwindow.h"
//            #include <QMessageBox>
//#include <QFileDialog>
//#include <QStringList>
//#include <QElapsedTimer>
//#include "AboutDialog.h"
//            #include <QFileInfo>
//
//
//#include <QDebug>
//
//    QAction* MainWindow::findMenuBarAction(QString text)
//    {
//        QAction* ret = nullptr;
//
//    const QObjectList list = menuBar()->children();     // menubar()返回当前主窗口的菜单栏，然后->children()找菜单栏下各个菜单QMenu
//
//        for(int i=0; i<list.length(); i++)
//        {
//            QMenu* menu = dynamic_cast<QMenu*>(list[i]);
//
//            if( menu != nullptr )
//            {
//                QList<QAction*> actions = menu->actions();  // menu->actions()找他存放子对象QAction*的链表
//
//                for(int j=0; j<actions.length(); j++)
//                {
//                    if( actions[j]->text() == text )
//                    {
//                        ret = actions[j];
//                        break;
//                    }
//                }
//            }
//        }
//
//        return ret;
//    }
//
//    void MainWindow::onFileOpen()
//    {
//        on_action_open_triggered();
//    }
//
//    void MainWindow::onFileFind()
//    {
//        //m_pFindDialog->show();
//    }
//
//    void MainWindow::onFileExit()
//    {
//        close();
//    }
//
//    void MainWindow::reverseCheckStatus(QString str1, QString str2)
//    {
//        QAction* action1 = findMenuBarAction(str1);
//        QAction* action2 = findMenuBarAction(str2);
//
//        bool check_status = action2->isChecked();
//
//        if( check_status )
//        {
//            action2->setChecked(false);
//        }
//        else
//        {
//            action1->setChecked(true);
//        }
//    }
//
//    void MainWindow::onCanToSig()
//    {
//        reverseCheckStatus("Can -> Sig", "Sig -> Can");
//    }
//
//    void MainWindow::onSigToCan()
//    {
//        reverseCheckStatus("Sig -> Can", "Can -> Sig");
//    }
//
//    void MainWindow::onTo_DBC()
//    {
//        reverseCheckStatus("To_DBC", "To_Excel");
//    }
//
//    void MainWindow::onTo_Excel()
//    {
//        reverseCheckStatus("To_Excel", "To_DBC");
//    }
//
//    void MainWindow::onAboutVersion()
//    {
//        AboutDialog dlg;
//
//        dlg.exec();
//    }
//
//    void MainWindow::showErrorMessage(QString messge)
//    {
//        QMessageBox msg(this);
//
//        msg.setWindowTitle("Error");
//        msg.setText(messge);
//        msg.setIcon(QMessageBox::Critical);
//        msg.setStandardButtons(QMessageBox::Ok);
//
//        msg.exec();
//    }
//
//    int MainWindow::showQueryMessage(QString message)
//    {
//        QMessageBox msg(this);
//
//        msg.setWindowTitle("Query");
//        msg.setText(message);
//        msg.setIcon(QMessageBox::Question);
//        msg.setStandardButtons(QMessageBox::Yes | QMessageBox::No);   // 并列按钮的情况用 | 来操作。
//
//        return msg.exec();  // 根据返回值来作出不同的操作响应
//    }
//
//
//    //　重写事件
//    void MainWindow::closeEvent(QCloseEvent* e)   // 重写关闭事件，保存配置属性
//    {
//        AppConfig config(pos(), size());
//
//        config.store();
//
//        QMainWindow::closeEvent(e);
//    }
//
//    void MainWindow::on_action_open_triggered()
//    {
//        qDebug() << "MainWindow::on_action_open_triggered()";
//        QString xlsFile = QFileDialog::getOpenFileName(this, QString(), QString(), "excel(*.xls *.xlsx)");  // 显示对话框，获取文件名
//
//        // 显示文件路径
//        QFileInfo fi(xlsFile);       // 以文件名初始化QFileInfo对象
//
//        m_filePath = fi.absoluteFilePath();    // 通过QFileInfo对象，获取文件的绝对路径
//
//        int r = showQueryMessage(QString("Do you want to Analysis this file? \nWhich is located at: \n") + m_filePath);
//
//        if( r == QMessageBox::No )
//            return;
//
//        if( xlsFile.isEmpty() )
//            return;
//
//        // 打开文件进行读取
//        QElapsedTimer timer;
//        timer.start();
//        if( m_xls.isNull() )
//            m_xls.reset(new ExcelBase);
//
//        m_xls->open(xlsFile);   // 打开文件
//        qDebug() << "open cost:" << timer.elapsed() << "ms"; timer.restart();
//
//        // 选定显示的sheet（此时下标对应从0开始的）
//        int sheet = m_standard.selectSheet(m_xls->sheetNames());
//        // qDebug() << sheet;
//
//        m_xls->setCurrentSheet(sheet+1);  // 设置当前读取的sheet，
//        m_xls->readAll(m_datas);    // 读取结束之后，m_datas就是一个QList< QList<QVariant> >对象，相当于Version1的m_QVariant
//        qDebug() << "read data cost:" << timer.elapsed() << "ms"; timer.restart();
//
//        // 保存选择的行列,此时就可以访问m_standard.indexTodisplay确定显示哪些列 done
//        m_standard.saveSelectCol(m_datas);
//
//        m_datas.removeAt(0);    // 删除第一行数字
//
//        // 此处的问题在于removeAt(i)之后链表的状态就变了，所以每次要删除的列的位置也在变
//        int colSize = m_datas.at(0).size();
//        for(int row=0; row<m_datas.size(); row++)
//        {
//            int mobileLen = 0;
//            for(int col=0; col<colSize; col++)
//            {
//                if( !m_standard.selectedCol(col) )
//                {
//                    m_datas[row].removeAt(col-mobileLen);
//                    mobileLen++;
//                }
//            }
//        }
//
//        QVariantListListModel* md = qobject_cast<QVariantListListModel*>(m_tableView.model());
//        if( md )
//        {
//            md->updateData();   // 显示！
//        }
//        qDebug() << "show data cost:" << timer.elapsed() << "ms"; timer.restart();
//    }
//
//    void MainWindow::on_action_write_triggered()
//    {
//        QString xlsFile = QFileDialog::getExistingDirectory(this);
//        if(xlsFile.isEmpty())
//            return;
//        xlsFile += "/excelRWByCztr1988.xls";
//        QElapsedTimer timer;
//        timer.start();
//        if(m_xls.isNull())
//            m_xls.reset(new ExcelBase);
//        m_xls->create(xlsFile);
//        qDebug()<<"create cost:"<<timer.elapsed()<<"ms";timer.restart();
//        QList< QList<QVariant> > m_datas;
//        for(int i=0;i<1000;++i)
//        {
//            QList<QVariant> rows;
//            for(int j=0;j<100;++j)
//            {
//                rows.append(i*j);
//            }
//            m_datas.append(rows);
//        }
//        m_xls->setCurrentSheet(1);
//        timer.restart();
//        m_xls->writeCurrentSheet(m_datas);
//        qDebug()<<"write cost:"<<timer.elapsed()<<"ms";timer.restart();
//        m_xls->save();
//    }
//
//
//#include "Mainwindow.h"
//
//    MainWindow::MainWindow() : m_tableView(this)//, import(new importFile())
//    {
//        m_filePath = "";
//        m_fileName = "";
//    }
//
//    MainWindow::~MainWindow()
//    {
//
//    }
//
//    bool MainWindow::construct()
//    {
//        bool ret = true;
//        AppConfig config;
//
//        ret = ret && initMenuBar();
//        ret = ret && initTableView();
//
//        if( config.isValid() )
//        {
//            resize(config.mainWindowSize());
//            move(config.mainWindowPoint());
//        }
//
//
//
//        return ret;
//    }
//
//    MainWindow* MainWindow::NewInstance()
//    {
//        MainWindow* ret = new MainWindow();
//
//        if( ret == nullptr || !(ret->construct()) )
//        {
//            delete ret;
//            ret = nullptr;
//        }
//
//        return ret;
//    }
//
//    bool MainWindow::initMenuBar()     // 初始化菜单栏和文本编辑区
//    {
//        bool ret = true;
//
//        QMenuBar* mb = menuBar();   // 从堆空间中申请一个菜单栏
//
//        ret = ret && initFileMenu(mb);
//        ret = ret && initEditMenu(mb);
//        ret = ret && initSetupMenu(mb);
//        ret = ret && initHelpMenu(mb);
//
//        return ret;
//    }
//
//    bool MainWindow:: initTableView()
//    {
//        bool ret = true;
//
//        setCentralWidget(&m_tableView);
//
//        QVariantListListModel* md = new QVariantListListModel(this);
//
//        m_tableView.setModel(md);     // view和model 视图和表格关联!!!
//
//        md->setVariantListListPtr(&m_datas);
//
//        return ret;
//    }
//
//    bool MainWindow::initFileMenu(QMenuBar* mb)    // 初始化文件菜单组
//    {
//        QMenu* menu = new QMenu("File(&F)", mb);
//        bool ret = (menu != nullptr);
//
//        if( ret )
//        {
//            QAction* action = nullptr;
//
//            ret = ret && makeAction(action, menu, "Open(&O)", Qt::CTRL + Qt::Key_O);
//
//            if( ret )
//            {
//                connect(action, SIGNAL(triggered()), this, SLOT(onFileOpen()));
//                menu->addAction(action);
//            }
//
//            menu->addSeparator();
//
//            ret = ret && makeAction(action, menu, "Find(&F)", Qt::CTRL + Qt::Key_F);
//
//            if( ret )
//            {
//                connect(action, SIGNAL(triggered()), this, SLOT(onFileFind()));
//                menu->addAction(action);
//            }
//
//            menu->addSeparator();
//
//            ret = ret && makeAction(action, menu, "Exit(&X)", 0);
//
//            if( ret )
//            {
//                connect(action, SIGNAL(triggered()), this, SLOT(onFileExit()));
//                menu->addAction(action);
//            }
//        }
//
//        if( ret )
//        {
//            mb->addMenu(menu);
//        }
//        else
//        {
//            delete menu;
//        }
//
//
//        return ret;
//    }
//
//    bool MainWindow::initEditMenu(QMenuBar* mb)
//    {
//        QMenu* menu = new QMenu("Edit(&E)", mb);
//        bool ret = (menu != nullptr);
//
//        if( ret )
//        {
//            QAction* action = nullptr;
//
//            ret = ret && makeAction(action, menu, "Can -> Sig", 0);
//
//            if( ret )
//            {
//                connect(action, SIGNAL(triggered()), this, SLOT(onCanToSig()));
//                action->setCheckable(true);
//                action->setChecked(true);
//                menu->addAction(action);
//            }
//
//            menu->addSeparator();
//
//            ret = ret && makeAction(action, menu, "Sig -> Can", 0);
//
//            if( ret )
//            {
//                connect(action, SIGNAL(triggered()), this, SLOT(onSigToCan()));
//                action->setCheckable(true);
//                action->setChecked(false);
//                menu->addAction(action);
//            }
//        }
//
//        if( ret )
//        {
//            mb->addMenu(menu);
//        }
//        else
//        {
//            delete menu;
//        }
//
//        return ret;
//    }
//
//    bool MainWindow::initSetupMenu(QMenuBar* mb)
//    {
//        QMenu* menu = new QMenu("Setup(&T)", mb);
//        bool ret = (menu != nullptr);
//
//        if( ret )
//        {
//            QAction* action = nullptr;
//
//            ret = ret && makeAction(action, menu, "To_Excel", 0);   // 父子关系，子类指定父亲setParent，加入一个指向父亲的指针；
//            //  同时，父类也要把子类加入自己的子对象指针链表addAction
//
//            if( ret )
//            {
//                connect(action, SIGNAL(triggered()), this, SLOT(onTo_Excel()));
//                action->setCheckable(true);
//                action->setChecked(true);
//                menu->addAction(action);
//            }
//
//            menu->addSeparator();
//
//            ret = ret && makeAction(action, menu, "To_DBC", 0);
//
//            if( ret )
//            {
//                connect(action, SIGNAL(triggered()), this, SLOT(onTo_DBC()));
//                action->setCheckable(true);
//                action->setChecked(false);
//                menu->addAction(action);
//            }
//        }
//
//        if( ret )
//        {
//            mb->addMenu(menu);
//        }
//        else
//        {
//            delete menu;
//        }
//
//        return ret;
//    }
//
//    bool MainWindow::initHelpMenu(QMenuBar* mb)
//    {
//        bool ret = true;
//
//        QMenu* menu = new QMenu("Help(&H)", mb);
//
//        ret = (menu != nullptr);
//
//        if( ret )
//        {
//            QAction* action = nullptr;
//
//            ret = ret && makeAction(action, this, "About(&A)", 0);
//
//            if( ret )
//            {
//                connect(action, SIGNAL(triggered()), this, SLOT(onAboutVersion()));
//
//                menu->addAction(action);
//            }
//        }
//
//        if( ret )
//        {
//            mb->addMenu(menu);
//        }
//        else
//        {
//            delete menu;
//        }
//
//        return ret;
//    }
//
//    bool MainWindow::makeAction(QAction*& action, QWidget* parent, QString text, int key)
//    {
//        bool ret = true;
//
//        action = new QAction(text, parent);
//
//        if( action != nullptr )
//        {
//            action->setShortcut(QKeySequence(key));
//        }
//        else
//        {
//            ret = false;
//        }
//
//        return ret;
//    }
//
//
//
//#ifndef QVARIANTLISTLISTMODEL_H
//#define QVARIANTLISTLISTMODEL_H
//
//#include <QAbstractTableModel>
//#include <QList>
//#include <QVector>
//    ///
///// \brief 用于显示Qvariant list list的model
///// Qvariant list list指代这个Qvariant list的元素也是Qvariant list，包含关系为
///// Qvariant list[Qvariant list]
/////
//    class QVariantListListModel : public QAbstractTableModel
//    {
//        Q_OBJECT
//
//        public:
//        explicit QVariantListListModel(QObject *parent = nullptr);
//
//        // Header:
//        QVariant headerData(int section, Qt::Orientation orientation, int role = Qt::DisplayRole) const override;
//
//        // Basic functionality:
//        int rowCount(const QModelIndex &parent = QModelIndex()) const override;
//        int columnCount(const QModelIndex &parent = QModelIndex()) const override;
//
//        QVariant data(const QModelIndex &index, int role = Qt::DisplayRole) const override;
//        void setVariantListListPtr(QList<QList<QVariant> >* ptr);
//        void updateData();
//        private:
//        QList<QList<QVariant> >* m_varListList;
//        //QVariantList* m_varListList;
//        int m_rowCount;///< 行数
//        int m_columnCount;///< 列数
//    };
//
//#endif // QVARIANTLISTLISTMODEL_H
//
//
//
//#include "QVariantListListModel.h"
//
//    QVariantListListModel::QVariantListListModel(QObject *parent)
//    : QAbstractTableModel(parent)
//    ,m_varListList(nullptr)
//    {
//    }
//
//    QVariant QVariantListListModel::headerData(int section, Qt::Orientation orientation, int role) const
//    {
//        if(Qt::DisplayRole == role)
//        {
//            if(Qt::Horizontal == orientation)
//            {
//                return section + 1;
//            }
//            return section + 1;
//        }
//        return QVariant();
//    }
//
//    int QVariantListListModel::rowCount(const QModelIndex &parent) const
//    {
//        Q_UNUSED(parent)
//        return m_rowCount;
//        // FIXME: Implement me!
//    }
//
//    int QVariantListListModel::columnCount(const QModelIndex &parent) const
//    {
//        Q_UNUSED(parent)
//        return m_columnCount;
//        // FIXME: Implement me!
//    }
//
//    QVariant QVariantListListModel::data(const QModelIndex &index, int role) const
//    {
//        if (!index.isValid())
//            return QVariant();
//        if(nullptr == m_varListList)
//            return QVariant();
//        if(Qt::DisplayRole == role)
//        {
//        const int row = index.row();
//        const int col = index.column();
//            if(row >= m_varListList->size())
//                return QVariant();
//        const int colSize = m_varListList->at(row).size();
//            if(col >= colSize)
//                return QVariant();
//            return m_varListList->at(row).at(col);
//        }
//        return QVariant();
//    }
//
//    void QVariantListListModel::setVariantListListPtr(QList<QList<QVariant> > *ptr)
//    {
//        if(nullptr == ptr)
//            return;
//        m_varListList = ptr;
//        updateData();
//    }
//
//    void QVariantListListModel::updateData()
//    {
//        beginResetModel();
//        m_rowCount = m_varListList->size();
//        m_columnCount = 0;
//        int colSize=0;
//        for(int i=0;i<m_rowCount;++i)
//        {
//            colSize = m_varListList->at(i).size();
//            if(colSize > m_columnCount)
//            {
//                m_columnCount = colSize;
//            }
//        }
//        endResetModel();
//    }
//
//#ifndef SELECTSTANDARD_H
//#define SELECTSTANDARD_H
//
//#include <QString>
//#include <QVector>
//#include <QList>
//#include <QVariant>
//#include <QAxObject>
//#include <QStringList>
//
//#define COL_NUMS 12
//
//    class SelectStandard
//    {
//        public:
//        QVector<QString> strVec;
//        QVector<int> indexToDisplay;
//
//        // 确定字符串是不是要显示的，参数是第一行的数据
//        bool needToDisplay(QString text);
//
//        // 保存列到数组中，参数是转换后的QList< QList<QVariant>
//        void saveSelectCol(QList< QList<QVariant> >& m_QVarRet );
//
//        // 选择读取的sheet，参数是所有sheet名
//        int selectSheet(QStringList sheetNames);
//        // 确定是不是要显示的列
//        bool selectedCol(int index);
//
//        public:
//        SelectStandard();
//    };
//
//#endif // SELECTSTANDARD_H
//
//
//#include "SelectStandard.h"
//
//
//    SelectStandard::SelectStandard()
//    {
//        strVec.push_back("Signal Name");
//        strVec.push_back("Msg ID");
//        strVec.push_back("GenMsgCycleTime");
//        strVec.push_back("Start Bit");
//        strVec.push_back("Signal Length (Bit)");
//        strVec.push_back("Byte Order");
//        strVec.push_back("Date Type");
//        strVec.push_back("Resolution");
//        strVec.push_back("Offset");
//        strVec.push_back("Signal Min. Value");
//        strVec.push_back("Signal Max. Value");
//        strVec.push_back("Signal Value Description");
//
//    }
//
//    // 选择sheet
//    int SelectStandard::selectSheet(QStringList sheetNames)
//    {
//        int selectSheet = 0;
//        for(int i=1; i<=sheetNames.size(); i++)     // 从1 还是0开始
//        {
//            if( sheetNames[i] == "Matrix" )
//            {
//                selectSheet = i;
//                break;
//            }
//        }
//
//        return selectSheet;
//    }
//
//    // 保存要显示的列
//    void SelectStandard::saveSelectCol(QList< QList<QVariant> >& m_QVarRet )
//    {
//        for(int j=0; j<m_QVarRet[1].size(); j++)
//        {
//            if( needToDisplay(m_QVarRet[1][j].toString()) )
//            {
//                if( indexToDisplay.length() < COL_NUMS )
//                {
//                    indexToDisplay.push_back(j);    // 一共要筛选的标识是12个，最后有一个同样是以...开始的快速周期，删除掉
//                }
//                else
//                {
//                    break;
//                }
//            }
//        }
//    }
//
//    // 判断是不是选中的列
//    bool SelectStandard::selectedCol(int index)
//    {
//        bool ret = false;
//
//        for(int i=0; i<indexToDisplay.length(); i++)
//        {
//            if( index == indexToDisplay[i] )
//            {
//                ret = true;
//                break;
//            }
//        }
//
//        return ret;
//    }
//
//    bool SelectStandard::needToDisplay(QString text)
//    {
//        bool ret = false;
//
//        for(int i=0; i<strVec.length(); i++)
//        {
//            if( text.startsWith(strVec[i]) )
//            {
//                ret = true;
//                break;
//            }
//        }
//
//        return ret;
//    }
//
//
//"Msg Name
//    报文名称"	"Signal Name
//    信号名称"	"Msg ID
//    报文标识符"	"Msg Length
//    报文长度"	"Transmit Node
//    发送节点"	"Signal Send Type
//    信号发送类型"	"Msg Send Type
//    报文发送类型"	"GenMsgCycleTime
//    报文周期时间"	"Start Bit
//    起始位"	Sig MSB	Sig LSB	"Signal Length (Bit)
//    信号长度"	"Byte Order
//    排列格式"	"Date Type
//    数据类型"	"Resolution
//    精度"	"Offset
//    偏移量"	"Signal Min. Value (phys)
//    物理最小值"	"Signal Max. Value(phys)
//    物理最大值"	"Unit
//    单位"	"Signal Value Table Name
//    信号值描述表名"	"Signal Value Description
//    信号值描述"	Signal comment注释	"GenSigTimeoutTime
//    信号超时时间"	"Signal Initial Value(Hex)
//    信号初始值"	"GenSigInactiveValue(Hex)
//    非使能值"	"GenMsgCycleTimeFast(ms)
//    报文发送的快速周期(ms)"	"Msg Nr. Of Reption
//    报文快速发送的次数"	"GenMsgDelayTime(ms)
//    报文延时时间(ms)"	GenMsgFastOnStart	GenMsgILSupport	NmMessage	DiagState	"诊断请求
//    DiagReq"	"诊断响应
//    DiagRsp"	DiagUudResponse	TpTxIndex	GenSigTImeoutValue	GenSigSuprvRespSubValue	NmAsrMessage	Message Comment	"Msg Type
//    报文类型"	"User Define Attribute
//    用户自定义属性"	iBDU	LSM	HVAC	iCGM
//    iBDU_281h	SystemPowerMode	0x281	8	iBDU	Cyclic	Cyclic	20	0	1	0	2	Intel	unsigned	1	0	0	3		Vtsig_SystemPowerMode	"0: $0=Off
//            1: $1=Accessory
//2: $2=Run
//3: $3=Crank Request"		60	0	0	20	0	0	0	yes	no	no	no	no		0	0		no		App			x	x	x
//    iBDU_281h	SystemPowerModeValidity	0x281	8	iBDU	Cyclic	Cyclic	20	2	2	2	1	Intel	unsigned	1	0	0	1		Vtsig_SystemPowerModeValidity	"0: Valid
//            1: Invalid"		60	0	0	20	0	0	0	yes	no	no	no	no		0	0		no		App			x	x	x
//    iBDU_281h	TurnLampLeftStatus	0x281	8	iBDU	Cyclic	Cyclic	20	3	3	3	1	Intel	unsigned	1	0	0	0		Vtsig_TurnLampLeftStatus	"0: Off
//            1: On"		60	0	0	20	0	0	0	yes	no	no	no	no		0	0		no		App			x		x
//
//
//
//}

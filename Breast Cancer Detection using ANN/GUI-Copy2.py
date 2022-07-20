#!/usr/bin/env python
# coding: utf-8

# In[4]:


from tkinter import * 
#from tkinter.ttk import *

def remove(widget1):
    root.grid_remove(widget1)
    
#check which dataset is chosen
def selected():
    print(v.get())
    if(v.get()=="1"):
        label=Label(root, text="WBCD Dataset", font=25).grid(column=1, row=3, pady = 2)
        print("WBCD Dataset")
    if(v.get()=="2"):
        label=Label(root, text="WDBC Dataset", font=25).grid(column=1, row=3, pady = 2)
        print("WDBC Dataset")
    
#if wbcd is chosen perform data preprocessing on the dataset
def wbcd_clicked():
    print(n1.get())
    
    #importing required libraries for data visualization and statistical operations
    import pandas as pd
    import numpy as np
    import matplotlib.pyplot as plt
    import seaborn as sb

    df=pd.read_csv('wbcd (1) - breast-cancer-wisconsin (1).csv')

    df['Class'].value_counts()

    plt.figure(figsize=[17,9])
    sb.countplot(df['Class'].value_counts())
    plt.show()

    #drop unnecessary column
    df=df.drop(['id number'],axis=1)

    missing_index=[]
    df.head()
    
    #check for missing attributes
    missing_index.append(df[df['Bare Nuclei'] == '?'].index)
    df = df.replace('?',np.nan)
    print(missing_index)
    print(df.isnull().sum())

    print(missing_index)
    print(([i for i in missing_index]))

    #create set with missing indices
    se = set([23, 40, 139, 145, 158, 164, 235, 249, 275, 292, 294, 297, 315, 321,
                411, 617])
    print(se)
    print(type(missing_index))
    print(df.isnull().sum())

    #drop target column from training set
    x = df.drop('Class',axis=1)
    
    #dependent variables
    y = df.Class

    print("\nX : ",x)
    print("\n\nY : ",y)

    from sklearn.preprocessing import LabelEncoder
    from sklearn.model_selection import train_test_split
    from sklearn.model_selection import KFold
    
    #creating the object
    lb = LabelEncoder()
    y = lb.fit_transform(y)

    if n1.get()=="ANN-WBCD":
        print("#0")
        
        #five fold cross validation
        kf = KFold(n_splits=5,shuffle=True, random_state=42)

        accuracy_list=[]
        x = x.dropna()
        ite = 0
        for train_index, test_index in kf.split(x.dropna()):

            print(set(train_index))

            tri = list(set(train_index) - se)
            tei = list(set(test_index) - se)
    
            print(type(set(train_index)))
            print(set(train_index))
            print("TRAIN:", train_index, "TEST:", test_index)
            xtrain=pd.DataFrame(x,index=tri)
            xtrain=pd.DataFrame(xtrain)
            xtest=pd.DataFrame(x,index=tei)


            print(list(set(train_index) - se))
            ytrain=df.loc[list(set(train_index) - se)].Class

            ytrain = lb.fit_transform(ytrain)
            print("\ndatatype of ytrain : ",type(ytrain))

            ytest=df.loc[tei].Class
            ytest = lb.fit_transform(ytest)


            from sklearn.preprocessing import StandardScaler
            #standardizing the dataset
            sc = StandardScaler()
            xtrain = sc.fit_transform(xtrain)
            xtest = sc.transform(xtest)

            import keras
            #importing sequential module
            from keras.models import Sequential
            # import dense module for hidden layers
            from keras.layers import Dense
            #importing activation functions
            from keras.layers import LeakyReLU,PReLU,ELU
            from keras.layers import Dropout

            classifier = Sequential()

            #hidden layer
            classifier.add(Dense(units=100,kernel_initializer='he_uniform',activation='relu',input_dim=9))
            
            #output layer
            classifier.add(Dense(units=1,kernel_initializer='glorot_uniform',activation='sigmoid'))

            classifier.summary()

            #compilation of the model
            classifier.compile(optimizer='adam',loss='binary_crossentropy',metrics=['accuracy'])

            #training the model
            model = classifier.fit(xtrain,ytrain,batch_size=100,epochs=100)

            #get prediction of the trained model
            y_pred = classifier.predict(xtest)

            y_pred = (y_pred>0.5)
            print(y_pred)

            pred_list = []

            correct=0
            for i in range(len(y_pred)):
                if y_pred[i] >= 0.5:
                    pred = 1
                else:
                    pred = 0

                if pred == ytest[i]:
                    correct += 1

                pred_list.append(pred)
            ite=ite+1

            accuracy_list.append((correct / len(ytest)) * 100)
            print('Test Accuracy : ', ((correct / len(ytest)) * 100), '%'," Iteration : ",ite)
            
            #calculation of accuracy and other preformance metrics
            from sklearn.metrics import confusion_matrix

            cf_matrix = confusion_matrix(ytest, y_pred)
            import seaborn as sns

            ax = sns.heatmap(cf_matrix, annot=True, cmap='Blues')

            ax.set_title('Confusion Matrix for ANN-WBCD model\n\n');
            ax.set_xlabel('\nPredicted Values')
            ax.set_ylabel('Actual Values ');

            ax.xaxis.set_ticklabels(['False','True'])
            ax.yaxis.set_ticklabels(['False','True'])

            plt.show()
            
            plt.show()
            
            from sklearn import metrics
            fpr, tpr, _ = metrics.roc_curve(ytest,  y_pred)

            #create ROC curve
            plt.plot(fpr,tpr)
            plt.ylabel('True Positive Rate')
            plt.xlabel('False Positive Rate')
            plt.show()
            
        print("\nAccuracies : \n", accuracy_list)
        print("\nAverage Accuracy : ",(sum(accuracy_list)/5))
        label.config(text="Accuracy of ANN-WBCD model : "+str(sum(accuracy_list)/5))
        
    if n1.get()=='GRNN (General Regression Neural Networks)':
        print("#1")
        #from neupy import algorithms
        from sklearn.model_selection import train_test_split
        from sklearn.model_selection import KFold
        kf = KFold(n_splits=5,shuffle=True, random_state=42)

        accuracy_list=[]
        x = x.dropna()
        ite = 0
        for train_index, test_index in kf.split(x.dropna()):

            print(set(train_index))

            print(train_index)
            print(test_index)
            tri = list(set(train_index) - se)
            tei = list(set(test_index) - se)

            print(type(set(train_index)))
            print(set(train_index))
            print("TRAIN:", train_index, "TEST:", test_index)
            xtrain=pd.DataFrame(x,index=tri)
            xtrain=pd.DataFrame(xtrain)
            xtest=pd.DataFrame(x,index=tei)


            print(list(set(train_index) - se))
            ytrain=df.loc[list(set(train_index) - se)].Class


            ytrain = lb.fit_transform(ytrain)

            ytest=df.loc[list(set(test_index)-se)].Class
            ytest = lb.fit_transform(ytest)
    

            from sklearn.preprocessing import StandardScaler
            #standardizing the dataset
            sc = StandardScaler()
            xtrain = sc.fit_transform(xtrain)
            xtest = sc.transform(xtest)

            #create the model
            nw = algorithms.GRNN(std=3, verbose=False)
            
            #train and test the model
            nw.train(xtrain, ytrain)
            
            #get prediction of the trained model
            y_pred = nw.predict(xtest)


            y_pred = (y_pred>0.5)
            print(y_pred)

            pred_list = []

            correct=0
            print(len(y_pred),"---------",len(ytest),len(xtrain),len(ytrain))
            for i in range(len(y_pred)):
                if y_pred[i] >= 0.5:
                    pred = 1
                else:
                    pred = 0

                if pred == ytest[i]:
                    correct += 1

                pred_list.append(pred)
            ite=ite+1

            accuracy_list.append((correct / len(ytest)) * 100)
            print('Test Accuracy : ', ((correct / len(ytest)) * 100), '%'," Iteration : ",ite)
            
            #calculation of accuracy and other preformance metrics
            from sklearn.metrics import confusion_matrix

            cm = confusion_matrix(ytest, y_pred)
            import seaborn as sns

            ax = sns.heatmap(cm, annot=True, cmap='Blues')

            ax.set_title('Confusion Matrix for GRNN model\n\n');
            ax.set_xlabel('\nPredicted Values')
            ax.set_ylabel('Actual Values ');

            ax.xaxis.set_ticklabels(['False','True'])
            ax.yaxis.set_ticklabels(['False','True'])
            
            plt.show()
            
        print("\nAccuracies : \n", accuracy_list)
        print("\nAverage Accuracy : ",(sum(accuracy_list)/5))
        print("\nBest Accuracy : ",max(accuracy_list))
        label1.config(text="Accuracy of GRNN model : "+str(max(accuracy_list)))
        
        
    if n1.get()=="GOANN (Genetically Optimized Neural Networks)":
        print("#2")
        from tpot import TPOTClassifier
        X = df.iloc[ : , 2:10 ].values
        Y = df.iloc[ : , -1].values
        l = LabelEncoder()
        Y = l.fit_transform(Y)
        print (X)
        print(Y)

        #split the dataset intp training and testing
        X_train, X_test, y_train, y_test = train_test_split(x, y, test_size = 0.3, random_state = 42)

        #create the model
        tpot = TPOTClassifier(generations=5 , verbosity=2, random_state=42)
        #train the model
        tpot.fit(X_train, y_train)

        print(tpot.score(X_test, y_test))

        #get prediction of the trained model
        y_pred = tpot.predict(X_test)

        y_pred = (y_pred>0.5)
        print(y_pred)

        correct=0
        for i in range(len(y_pred)):
            if y_pred[i] >= 0.5:
                pred = 1
            else:
                pred = 0

            if pred == y_test[i]:
                correct += 1


        print('Test Accuracy : ', ((correct / len(y_test)) * 100))
        label1.config(text="Accuracy of GOANN model: "+str((correct / len(y_test)) * 100), font=25).grid(column=2, row=4, pady = 2)
        
        #calculation of accuracy and other preformance metrics
        from sklearn.metrics import confusion_matrix

        #Generate the confusion matrix
        cm = confusion_matrix(y_test, y_pred)

        import seaborn as sns

        ax = sns.heatmap(cm, annot=True, cmap='Blues')

        ax.set_title('Confusion Matrix for GOANN model\n\n');
        ax.set_xlabel('\nPredicted Values')
        ax.set_ylabel('Actual Values ');

        ax.xaxis.set_ticklabels(['False','True'])
        ax.yaxis.set_ticklabels(['False','True'])

        plt.show()

def wdbc_clicked():
    #importing required libraries for data visualization and statistical operations
    import pandas as pd
    import numpy as np
    import matplotlib.pyplot as plt
    import seaborn as sb

    #importing sklearn library
    from sklearn.preprocessing import LabelEncoder
    from sklearn.model_selection import train_test_split
    from sklearn.model_selection import KFold
    from sklearn.preprocessing import StandardScaler
    
    from sklearn.metrics import classification_report
    from sklearn.metrics import confusion_matrix
    from sklearn.metrics import accuracy_score
    from sklearn.metrics import precision_recall_fscore_support


    #importing keras library
    import keras

    #importing sequential module
    from keras.models import Sequential

    # import dense module for hidden layers
    from keras.layers import Dense

    #importing activation functions
    from keras.layers import LeakyReLU,PReLU,ELU
    from keras.layers import Dropout

    #loading the dataset
    df = pd.read_csv('wdbc - 32.csv')
    print(df.dtypes)

    df['diagnosis'].value_counts()

    plt.figure(figsize=[17,9])
    sb.countplot(df['diagnosis'].value_counts())
    plt.show()

    #check for missing attributes in the dataset
    df.isnull().sum()

    #drop unnecessary column from the dataset
    df.drop(['id'],axis=1,inplace=True)
    x = df.drop('diagnosis',axis=1)

    #dependent variables
    y = df.diagnosis

    #encoding target labels
    lb = LabelEncoder()

    print(n2.get())
    if n2.get()=="ANN-WDBC":
        print("#0")
        accuracy_list=[]
        ite=0

        #five fold cross validation
        kf = KFold(n_splits=5, shuffle=True, random_state=42)

        for train_index, test_index in kf.split(x):
            xtrain=[]
            ytrain1=[]
            xtest=[]
            ytest=[]
            print("TRAIN:", train_index, "TEST:", test_index)
            xtrain=pd.DataFrame(x,index=train_index)
            xtest=pd.DataFrame(x,index=test_index)
            ytrain=df.loc[train_index].diagnosis
            ytrain = lb.fit_transform(ytrain)
            ytest=df.loc[test_index].diagnosis
            ytest = lb.fit_transform(ytest)
            print("Train : \n",xtrain)
            print("Test : \n",xtest)
            print("Train : \n",ytrain)
            print("Test : \n",ytest)

            sc = StandardScaler()
            xtrain = sc.fit_transform(xtrain)
            xtest = sc.transform(xtest)

            classifier = Sequential()

            #Hidden layer
            classifier.add(Dense(units=100,kernel_initializer='he_uniform',activation='relu',input_dim=30))

            #Output layer
            classifier.add(Dense(units=1,kernel_initializer='glorot_uniform',activation='sigmoid'))

            classifier.summary()

            classifier.compile(optimizer='adam',loss='binary_crossentropy',metrics=['accuracy'])

            #training the model
            model = classifier.fit(xtrain,ytrain,batch_size=100,epochs=100)

            #get the predictions from the trained model
            y_pred = classifier.predict(xtest)

            y_pred = (y_pred>0.5)
            print(y_pred)


            pred_list = []
            correct=0
            for i in range(len(y_pred)):
                if y_pred[i] >= 0.5:
                    pred = 1
                else:
                    pred = 0

                if pred == ytest[i]:
                    correct += 1
                pred_list.append(pred)
            ite=ite+1

            accuracy_list.append((correct / len(ytest)) * 100)
            print('Test Accuracy : ', ((correct / len(ytest)) * 100), '%'," Iteration : ",ite)
            
            #calculation of accuracy and other preformance metrics
            from sklearn.metrics import confusion_matrix

            #Generate the confusion matrix
            cm = confusion_matrix(ytest, y_pred)
            import seaborn as sns

            ax = sns.heatmap(cm, annot=True, cmap='Blues')

            ax.set_title('Confusion Matrix for ANN-WDBC model\n\n');
            ax.set_xlabel('\nPredicted Values')
            ax.set_ylabel('Actual Values ');

            ax.xaxis.set_ticklabels(['False','True'])
            ax.yaxis.set_ticklabels(['False','True'])

            plt.show()
            
            from sklearn import metrics
            fpr, tpr, _ = metrics.roc_curve(ytest,  y_pred)

            #create ROC curve
            plt.plot(fpr,tpr)
            plt.ylabel('True Positive Rate')
            plt.xlabel('False Positive Rate')
            plt.show()
        
        print("\nAccuracies : \n",accuracy_list)
        print("\nAverage Accuracy : ",(sum(accuracy_list)/5))
        label.config(text="Accuracy of ANN-WDBC model: "+str(sum(accuracy_list)/5))
        
        
    if n2.get()=="MLP (Multilayer Perceptron Neural Networks)":
        print("#1")
        y = lb.fit_transform(y)
        from sklearn.neural_network import MLPClassifier 
        
        #split the dataset into training and testing
        x_train, x_test, y_train, y_test = train_test_split(x, y, test_size = 0.30, random_state=40) 

        #standardization of dataset
        scaler = StandardScaler()  
        
        #train the model
        scaler.fit(x_train)
        x_train = scaler.transform(x_train)  
        x_test = scaler.transform(x_test)
        
        clf = MLPClassifier(hidden_layer_sizes=(500, 500, 500), activation="relu", batch_size=128, learning_rate_init=0.01, random_state=42, max_iter=1000).fit(x_train, y_train)
        clf.predict_proba(x_test[:1])

        #Get predictions from the trained model
        predictions = clf.predict(x_test) 
        
        print(type(predictions))
        
        pred_list = []
        correct=0
        for i in range(len(predictions)):
            if float(predictions[i]) >= 0.5:
                pred = 1
            else:
                pred = 0

            if pred == y_test[i]:
                correct += 1

            pred_list.append(pred)

        print('Test Accuracy : ', ((correct / len(y_test)) * 100), '%')


        print(predictions)

        print(confusion_matrix(y_test,predictions))  
        print(classification_report(y_test,predictions))

        #calculation of accuracy and other preformance metrics
        cm = confusion_matrix(y_test,predictions)
        score = accuracy_score(y_test,predictions)
        print("Confusion matrix : \n",cm)
        print('score is:',score)


        precision, recall,fscore, support =  precision_recall_fscore_support(y_test, pred_list, average=None)

        tn, fp, fn, tp = confusion_matrix(y_test, pred_list).ravel()
        print('True Nagative', tn)
        print('False Positive', fp)
        print('False Negative', fn)
        print('True Positive', tp)

        total = tn + tp + fn + fp

        print('Test Accuracy : ', (tn + tp)/total)
        print('Missclassification Rate : ', (fn + fp)/total)
        print('precision : ', precision)
        print('recall : ', recall)
        print('FScore : ', fscore)
        print('Support : ', support)
        print('Sensitivity or TPR : ', (tp/ (tp + fn)) )
        print('Specificity or TNR : ', (tn/(tn+fp)))
        print('False Positive Rate or Fallout : ', (fp/(fp+tn)))
        print('False Negative Rate : ', (fn/(fn+tp)))

        print('False Discovery Rate : ', (fp/(tp+fp)))
        label1.config(text="Accuracy of MLP model : "+str(((tn + tp)/total)*100))
        import seaborn as sns

        ax = sns.heatmap(cm, annot=True, cmap='Blues')

        ax.set_title('Confusion Matrix for MLP model\n\n');
        ax.set_xlabel('\nPredicted Values')
        ax.set_ylabel('Actual Values ');

        ax.xaxis.set_ticklabels(['False','True'])
        ax.yaxis.set_ticklabels(['False','True'])

        plt.show()

    if n2.get()=="ANN Wrapper":
        print("#2")
        y = lb.fit_transform(y)
        
        from mlxtend.feature_selection import SequentialFeatureSelector as SFS
        from sklearn.linear_model import LinearRegression

        # Sequential Forward Selection(sfs)
        sfs = SFS(LinearRegression(),
                  k_features=11,
                  forward=True,
                  floating=False,
                  scoring = 'r2',
                  cv = 0)

        
        sfs.fit(x, y)
        sfs.k_feature_names_
        print(df.columns)
        sel_col=sfs.k_feature_names_
        print(sel_col)
        for column in x.columns:
            if(column not in sel_col):
                x=x.drop(column, axis=1)


        print(x.columns)

        #split the dataset into training and testing
        xtrain,xtest,ytrain,ytest = train_test_split(x,y,test_size=0.2,random_state=42)

        #standardizing the dataset
        sc = StandardScaler()
        xtrain = sc.fit_transform(xtrain)
        xtest = sc.transform(xtest)

        #creating the model
        classifier = Sequential()

        #Adding the Hidden layer
        classifier.add(Dense(units=100,kernel_initializer='he_uniform',activation='relu',input_dim=11))

        #Adding the output layer
        classifier.add(Dense(units=1,kernel_initializer='glorot_uniform',activation='sigmoid'))

        classifier.summary()

        #compilation of the model
        classifier.compile(optimizer='adam',loss='binary_crossentropy',metrics=['accuracy'])

        #training the model
        model = classifier.fit(xtrain,ytrain,batch_size=100,epochs=100)

        #get prediction of the trained model
        y_pred = classifier.predict(xtest)

        y_pred = (y_pred>0.5)
        print(y_pred)

        #calculation of accuracy and other preformance metrics
        cm = confusion_matrix(ytest,y_pred)
        score = accuracy_score(ytest,y_pred)
        print("Confusion matrix : \n",cm)
        print('score is:',score)
        pred_list = []
        correct=0
        for i in range(len(y_pred)):
                    if y_pred[i] >= 0.5:
                        pred = 1
                    else:
                        pred = 0

                    if pred == ytest[i]:
                        correct += 1

                    pred_list.append(pred)

        print('Test Accuracy : ', ((correct / len(ytest)) * 100), '%')


        #calculation of accuracy and other preformance metrics
        precision, recall,fscore, support =  precision_recall_fscore_support(ytest, pred_list, average=None)

        tn, fp, fn, tp = confusion_matrix(ytest, pred_list).ravel()
        print('True Nagative', tn)
        print('False Positive', fp)
        print('False Negative', fn)
        print('True Positive', tp)

        total = tn + tp + fn + fp

        print('Test Accuracy : ', (tn + tp)/total)
        print('Missclassification Rate : ', (fn + fp)/total)
        print('precision : ', precision)
        print('recall : ', recall)
        print('FScore : ', fscore)
        print('Support : ', support)
        print('Sensitivity or TPR : ', (tp/ (tp + fn)) )
        print('Specificity or TNR : ', (tn/(tn+fp)))
        print('False Positive Rate or Fallout : ', (fp/(fp+tn)))
        print('False Negative Rate : ', (fn/(fn+tp)))
        print('False Discovery Rate : ', (fp/(tp+fp)))
        label1.config(text="Accuracy of ANN Wrapper model: "+str(((tn + tp)/total)*100))
        
        import seaborn as sns

        ax = sns.heatmap(cm, annot=True, cmap='Blues')

        ax.set_title('Confusion Matrix for ANN Wrapper model\n\n');
        ax.set_xlabel('\nPredicted Values')
        ax.set_ylabel('Actual Values ');

        ax.xaxis.set_ticklabels(['False','True'])
        ax.yaxis.set_ticklabels(['False','True'])

        plt.show()
    

#GUI creation
root = Tk()

root.title("Breast cancer prediction")

root.geometry('600x450')

root.columnconfigure(0, weight=1)
root.columnconfigure(1, weight=2)
root.columnconfigure(2, weight=1)

root.rowconfigure(0, weight=1)
root.rowconfigure(1, weight=1)
root.rowconfigure(2, weight=1)
root.rowconfigure(3, weight=1)
root.rowconfigure(4, weight=2)
#root.resizable(False, False)
v = StringVar(root, "1")

label=Label(root, text="Artificial Neural Networks", font=25).grid(column=1, row=0,pady = 2)

#style = Style(root)
#style.configure("Radiobutton",font = ("arial", 20, "bold"))

Radiobutton(root, text = "WBCD (Wisconsin Breast Cancer Dataset)", variable = v,
        value = "1", command=selected).grid(column=0, row=1)

Radiobutton(root, text = "WDBC (Wisconsin Diagnostic Breast cancer)", variable = v,
        value = "2", command=selected).grid(column=0, row=2)

label=Label(root, text="WBCD Dataset", font=25).grid(column=1, row=3, pady = 2)

n1 = StringVar()
wbcd_models = Combobox(root, width = 27, 
                            textvariable = n1)
  
# Adding combobox drop down list
wbcd_models['values'] = ('ANN-WBCD', 'GRNN (General Regression Neural Networks)', 'GOANN (Genetically Optimized Neural Networks)')

wbcd_models.grid(column=1, row=1)

wbcd_models.current(0)

btn = Button(root, text = 'Calculate Accuracy', height=5, width=30,command = wbcd_clicked)
 

btn.grid(column=2, row=1)

n2 = StringVar()
wdbc_models = Combobox(root, width = 27, 
                            textvariable = n2)
  
# Adding combobox drop down list
wdbc_models['values'] = ('ANN-WDBC', 'MLP (Multilayer Perceptron Neural Networks)', 'ANN Wrapper')

wdbc_models.grid(column=1, row=2)

wdbc_models.current(0)

btn = Button(root, text = 'Calculate Accuracy', height=5, width=30,command =wdbc_clicked)
 
btn.grid(column=2, row=2)

label=Label(root, text="", font=25)
label.grid(column=0, row=4, pady = 2)
label.config(text="")
label1=Label(root, text="", font=25)
label1.grid(column=2, row=4, pady = 2)
label1.config(text="")
#label2=Label(root, text="", font=25).grid(column=1, row=4, pady = 2)
root.mainloop()


# In[ ]:





# In[ ]:




